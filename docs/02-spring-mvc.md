# 02) Spring MVC 기본

## 서버 / Controller
- 서버: 요청(Request)을 받고 응답(Response)을 보내는 프로그램
- Controller: URL 요청을 받아 어떤 응답을 줄지 결정

## WebMVC 의존성
`@GetMapping`, `@ResponseBody`는 WebMVC가 필요

```gradle
implementation 'org.springframework.boot:spring-boot-starter-webmvc'
```

## 텍스트 응답

```java
@Controller
public class BasicController {

@GetMapping("/hello")
@ResponseBody
  String hello() {
    return "유저에게 보내줄 데이터";
  }
}
```

## HTML 응답 (템플릿/뷰)

- `resources/templates`: 템플릿 렌더링
- `resources/static`: 정적 파일

```java
@GetMapping("/")
String index() {
    return "index";// templates/index.html
}

```

## HTTP Method 정리

- GET: 조회
- POST: 생성
- PUT: 전체 수정
- PATCH: 부분 수정
- DELETE: 삭제

## REST URL 설계 팁

- 동사보다 명사 (`/items`)
- 복수형 선호 (`/users`)
- 단건 리소스: `/items/{id}`
- 하위 리소스: `/users/{id}/orders`
- 확장자 X
