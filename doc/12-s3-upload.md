# S3 이미지 업로드 (Presigned URL 방식)

서버가 파일을 직접 업로드하지 않고, **클라이언트가 S3로 직접 PUT 업로드**하도록 허용하는 방식.  
서버는 업로드를 위한 **Presigned URL만 발급**해준다.

- 장점: 서버 트래픽/부하 감소, 대용량 업로드에 유리
- 단점: 업로드 파일 검증(확장자/용량/내용) 제어가 어렵고, 추가 처리(리사이즈/압축)는 별도 설계 필요

---

## 1) AWS S3 세팅

### 1-1) 버킷 만들기
- AWS S3 → **Bucket 생성**
- 버킷명 예: `springmall-bom2zzang`
- 리전: `ap-northeast-2(Seoul)` 등

> 버킷 = 가상 하드디스크(스토리지)

---

### 1-2) 버킷 정책(Bucket Policy)

#### 공개 조회(GetObject) 허용 (선택)
브라우저에서 이미지를 **public URL로 바로 조회**하고 싶다면 필요할 수 있음.  
(단, 운영에서는 보안 정책 잘 고민해서 적용)

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PublicRead",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::버킷명/*"
    }
  ]
}
```
### Put/Delete는 root 대신 IAM 정책으로 권장

버킷 정책에서 `PutObject/DeleteObject`를 열기보단,

**IAM 사용자/역할(Role)에 최소권한 정책 부여**가 권장됨.

---

### 1-3) CORS 설정 (버킷 CORS)

브라우저에서 S3로 직접 업로드(PUT)하려면 CORS가 필요.

```json
[
    {
        "AllowedHeaders":["*"],
        "AllowedMethods":["PUT","POST","GET"],
        "AllowedOrigins":["*"],
        "ExposeHeaders":["ETag"]
    }
]

```

- `AllowedOrigins`는 운영에서는  대신 실제 도메인만 넣는 걸 추천

  예: `["https://my-service.com"]`

- PUT 업로드를 쓰면 `PUT`은 필수
- 이미지를 브라우저에서 조회한다면 `GET`도 추가하는 편이 안전

---

## 2) IAM Access Key 발급

- IAM → 사용자 생성
- **Access Key / Secret Key 발급**
- 권한은 최소로(Least privilege) 부여 권장
    - 예: 특정 버킷에 대해 `s3:PutObject`, `s3:GetObject` 정도

> Access Key/Secret Key는 절대 GitHub에 커밋하면 안 됨
>
>
> `.env`, GitHub Secrets, 서버 환경변수로 관리 추천
>

---

## 3) Spring Boot 라이브러리 설치

`build.gradle`

```
implementation 'io.awspring.cloud:spring-cloud-aws-starter-s3:3.1.1'
```

---

## 4) application.properties 설정

```
spring.cloud.aws.credentials.accessKey=${AWS_ACCESS_KEY}
spring.cloud.aws.credentials.secretKey=${AWS_SECRET_KEY}

spring.cloud.aws.s3.bucket=버킷명
spring.cloud.aws.region.static=ap-northeast-2
```

- `${AWS_ACCESS_KEY}`, `${AWS_SECRET_KEY}`는 환경변수로 주입

---

## 5) Presigned URL 방식 개념

### 업로드 흐름

1. 유저가 이미지 선택
2. 클라이언트가 서버에 “파일명(path)”으로 Presigned URL 요청
3. 서버가 Presigned URL 발급(유효기간 짧게)
4. 클라이언트가 Presigned URL로 S3에 **PUT 업로드**
5. 업로드된 파일 URL(혹은 key)을 DB에 저장해서 재사용

### 단점/주의점

- 서버가 파일 내용을 직접 받지 않으므로 **이상한 파일 업로드를 완전히 막기 어려움**
- 이미지 리사이즈/압축이 필요하면:
    - 프론트에서 압축/리사이즈 후 업로드
    - 또는 S3 이벤트 + Lambda 등 서버리스로 후처리

---

## 6) Presigned URL 발급 서비스 예시 (Spring)

```java
@Service
@RequiredArgsConstructor
public class S3Service {

@Value("${spring.cloud.aws.s3.bucket}")
private String bucket;

private final S3Presigner s3Presigner;

// presigned URL 생성 (PUT)
public String createPresignedUrl(String path) {
var putObjectRequest= PutObjectRequest.builder()
        .bucket(bucket)
        .key(path)// 예: "uploads/uuid-file.png"
        .build();

var preSignRequest= PutObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(3))// URL 유효기간
        .putObjectRequest(putObjectRequest)
        .build();

return s3Presigner.presignPutObject(preSignRequest).url().toString();
  }
}
```
