# 10) DTO로 Object → JSON 변환하기

서버에서 데이터를 응답으로 보낼 때 보통 **JSON** 형태로 내려준다.  
이때 “어떤 형태로 데이터를 담아 보낼지”를 정리한 것이 DTO(Data Transfer Object)다.

---

## 10-1) Map으로 응답 만들기 (간단/빠름)

전달할 값이 적고 임시로 테스트할 때는 `Map`으로도 쉽게 보낼 수 있다.

```java
Map<String, Object> map = new HashMap<>();
map.put("displayName", a.get().getDisplayName());
map.put("username", a.get().getUsername());

return map; // (컨트롤러에서 @ResponseBody or @RestController)
```
장점
- 빠르게 만들 수 있음

단점
- 타입 안정성이 약함 (키 오타/타입 실수)
- 재사용/문서화가 어려움

---

## 10-2) DTO 클래스로 응답 만들기 (추천)

전달하고 싶은 필드만 담은 클래스를 만들어서 반환하면, Spring이 자동으로 JSON으로 변환한다.


### DTO 클래스 만들기

```java
public class Data {
public String username;
public String displayName;
}
```

### 생성자(constructor) 추가하기

```java
public class Data {
public String username;
public String displayName;

public Data(String username, String displayName) {
this.username = username;
this.displayName = displayName;
  }
}
```

### 사용 예시

```java
Data data = new Data(username, displayName);
return data;
```

주의
- Spring이 JSON 변환하려면 **필드 접근이 가능**해야 한다.
    - `public` 필드로 두거나
    - `private`이면 **getter**가 있어야 함

예: Lombok 사용

```java
@Getter
@AllArgsConstructor
public class Data {
private String username;
private String displayName;
}
```

---

## 10-3) DTO의 장점

1. **타입 체크 가능**
    - 잘못된 타입/필드명 실수를 컴파일 단계에서 줄일 수 있음
2. **재사용 가능**
    - 여러 API에서 같은 응답 형태를 반복 사용 가능
3. **역할 분리**
    - Entity(DB 모델)와 Response 모델(DTO)을 분리해서 안전하게 설계 가능

---

## 10-4) DTO 매핑 라이브러리 (MapStruct 등)

Entity → DTO 변환을 많이 하게 되면 “매핑 코드”가 반복된다.

이걸 자동화해주는 대표 라이브러리가 **MapStruct**.

- 장점: 코드 생성 기반이라 성능 좋고 타입 안정성 좋음
- 단점: 설정이 조금 필요

(실무에선 MapStruct / ModelMapper 등 선택해서 사용)

---

## 10-5) record (Java 14+): DTO를 더 짧게 만들기

Java 14+에서는 `record`를 사용해 DTO를 매우 간단히 만들 수 있다.

```java
public record Person(String name, Integer age) {}
```

이 한 줄로 아래가 자동 생성된다:

- 필드(name, age)
- 생성자
- getter(정확히는 `name()`, `age()` 형태의 접근자)
- `toString()`, `equals()`, `hashCode()`

특징

- **불변(immutable)**: setter 없음 → 값 변경 불가
- DTO 용도로 매우 적합

---

## 언제 뭘 쓰면 좋을까?

- 빠르게 테스트: **Map**
- 실무/정석/유지보수: **DTO 클래스**
- DTO를 많이 만들기 귀찮고 불변이면 OK: **record**
