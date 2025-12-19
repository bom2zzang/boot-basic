# boot-basic
## **1) IntelliJ / 실행 단축키**

- `sout` + Enter : `System.out.println()`
- Run: `Ctrl + R`(Mac은 `Control + R` 또는 상단 Run)
- Rebuild/Compile: `⌘ + F9`
- Gradle 재동기화(중요): Gradle 탭 → Reload / 또는 `./gradlew build`

---

## 2) Java 기초 문법

### 타입

- `String`
- `int` / `Integer`
- `long` / `Long`
- `float` / `Float`
- `double` / `Double`
- `boolean` / `Boolean`

**primitive vs wrapper**

- `int, long, boolean` 같은 건 **primitive(기본형)**
- `Integer, Long, Boolean`은 **wrapper(객체형)**
- DB/JPA에서는 보통 **wrapper를 선호** (null 허용 등)

### `var` (Java 10+)

- **컴파일 시 타입 추론** (런타임에 바뀌는 동적 타입이 아님)
- 지역 변수에서만 사용 가능

### `final`

- 값 재할당 불가
- 실무에선 “변경되면 안 되는 값”을 명확하게 하려고 많이 씀

---

## 3) 클래스 / 객체 / 생성자

- class: 변수(field) + 함수(method) 묶음
- object(인스턴스): `new`로 만든 클래스의 실제 값

```java
class Test {
  String name;
}

Test test = new Test();
test.name = "bom";
```

### constructor(생성자)

- 객체 만들 때 자동 실행되는 “초기화 함수”
- 클래스명과 동일한 이름

```java
class Car {
  String model = "hi";
  int price;

  Car(int price) {
    this.price = price;
  }
}

Car car1 = new Car(300000);
```

> this.price = “이 객체의 price”
>

---

## 4) 서버 / Controller 개념

- 서버: 요청(Request)을 받고 응답(Response)을 보내는 프로그램
- Controller: URL 요청을 받아서 “어떤 응답을 줄지” 결정하는 곳

---

## 5) Spring MVC 기본

### WebMVC 의존성 필요

`@GetMapping`, `@ResponseBody`는 WebMVC가 있어야 인식됨

```
implementation 'org.springframework.boot:spring-boot-starter-webmvc'
```

### 텍스트 응답

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

### HTML 응답 (템플릿/뷰)

- `resources/templates`에 있는 파일은 “뷰 렌더링”
- `resources/static`에 있는 파일은 “그냥 정적 파일”

```java
@GetMapping("/")
String index() {
  return "index"; // templates/index.html
}
```

정적 파일이면:

- `resources/static/index.html`은 그냥 브라우저가 바로 가져감 (`/index.html`)

---

## 6) API Method 정리

- GET: 조회
- POST: 생성
- PUT: 전체 수정(체)
- PATCH: 부분 수정
- DELETE: 삭제

---

## 7) REST API 규칙 (보강)

### URL 설계 팁

- 동사보다 **명사**
- 복수형 선호: `/items`, `/users`
- 특정 1개: `/items/{id}`
- 하위 리소스: `/users/{id}/orders`
- 확장자 X (`.do`, `.php` 같은 거 X)

---

## 8) Thymeleaf 템플릿 엔진

### 의존성

```
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
```

### 모델 전달

```java
@GetMapping("/list")
String list(Model model) {
  model.addAttribute("name", "홍길동");
  return "list";
}
```

HTML:

```html
<h4 th:text="${name}"></h4>
```

### 반복문

```html
<div th:each="item : ${items}">
  <h4 th:text="${item.title}"></h4>
</div>
```

> ${items}는 Controller에서 model.addAttribute("items", result)로 넣어줘야 함
>

---

## 9) DB / JPA / Hibernate

### 개념

- SQL 직접 작성 대신, 자바 객체로 DB를 다루는 방식 = ORM
- JPA = ORM 표준 스펙
- Hibernate = JPA 구현체(실제로 동작하는 라이브러리)

### 의존성

```
runtimeOnly 'com.mysql:mysql-connector-j'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
```

- `runtimeOnly`: 실행할 때만 필요

### application.properties 설정

```
spring.datasource.url=jdbc:mysql://주소/DB이름
spring.datasource.username=아이디
spring.datasource.password=비번

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.show_sql=true
```

✅ `ddl-auto` 옵션 의미

- `create`: 실행할 때마다 테이블 새로 생성(데이터 날아감)
- `update`: 있으면 유지 + 변경 반영
- `validate`: 검증만, 변경 X
- `none`: 아무것도 안함

---

## 10) Entity(테이블) 만들기

```java
@Entity
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  public String title;
  public Integer price;
}
```

- field는 보통 `private`
- getter/setter 또는 lombok 사용
- 컬럼 제약은 `@Column`로 가능

```java
@Column(nullable = false, unique = true)
private String title;
```

---

## 11) Repository로 DB 조회

```java
public interface ItemRepository extends JpaRepository<Item, Long> {
}
```

조회 예시:

```java
List<Item> items = itemRepository.findAll();
```

---

## 12) DI(의존성 주입) / @Autowired / @RequiredArgsConstructor

요즘 가장 많이 쓰는 방식:

```java
@RequiredArgsConstructor
@Controller
public class ItemController {
  private final ItemRepository itemRepository;
}
```

---

## 13) Lombok

의존성:

```
compileOnly 'org.projectlombok:lombok'
annotationProcessor 'org.projectlombok:lombok'
```

자주 쓰는 어노테이션:

- `@Getter`, `@Setter`
- `@ToString`
- `@NoArgsConstructor`, `@AllArgsConstructor`
- `@RequiredArgsConstructor`

> IntelliJ에서 Lombok 안 먹으면:
>
>
> **Settings → Plugins → Lombok 설치**,
>
> **Annotation Processing 활성화** 필요
>

---

## 14) 접근제어자 / static

- `public`: 어디서나 접근
- (default / package-private): 같은 패키지에서만
- `private`: 클래스 내부에서만
- `protected`: 같은 패키지 + 상속받은 클래스

### `static`

- 객체 없이 클래스명으로 바로 사용
- 유틸 함수에 자주 씀

```java
Math.max(1, 2);
```

---

## 15) Getter/Setter 쓰는 이유

검증 로직을 넣을 수 있어서.

```java
public void setPrice(Integer price) {
  if (price < 0) throw new IllegalArgumentException("가격은 음수 불가");
  this.price = price;
}
```

---