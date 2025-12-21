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
## 16) 상품 추가 기능 (Write → Add)

### 글 작성 페이지 라우팅

```java
@GetMapping("/write")
String write() {
  return "write"; // templates/write.html
}
```

---

### write.html 폼 만들기

```html
<form action="/add" method="post">
  <input name="title" />
  <input name="price" />
  <button type="submit">등록</button>
</form>
```

✅ 포인트

- `name="title"`, `name="price"`가 서버 파라미터명(또는 DTO/Entity 필드명)과 매칭되어야 바인딩됨
- 버튼은 `type="submit"`이어야 폼이 전송됨 (`button` 기본 타입은 브라우저마다 다르게 동작할 수 있어 명시 추천)

---

### 전송: 서버에서 파라미터 받기 (@RequestParam)

```java
@PostMapping("/add")
String addPost(@RequestParam String title, @RequestParam Integer price) {
  // 저장 로직 ...
  return "redirect:/list";
}
```

- `redirect:/list`는 **서버 렌더링(폼 제출) 방식에서 페이지 이동 가능**
- **AJAX로 호출한 경우**에는 `redirect:`로 화면 이동이 자동으로 되지 않음

  (AJAX는 응답을 “데이터”로 받기 때문 → 프론트에서 직접 이동 처리 필요)


---

## 17) AJAX로 보낸 데이터 처리 (@RequestBody)

- form 전송(`application/x-www-form-urlencoded`)은 `@RequestParam`/`@ModelAttribute`로 잘 받음
- AJAX(JSON)로 body에 담아서 보낸 데이터는 보통 `@RequestBody`로 받음

예시(JSON 요청을 받을 때):

```java
@PostMapping("/add")
String addPost(@RequestBody Map<String, Object> body) {
  System.out.println(body);
  return "ok";
}
```

> JSON으로 받을 땐 보통 DTO 클래스를 만들어 @RequestBody ItemRequest dto처럼 받는 방식이 더 많이 쓰임.
>

---

## 18) Map 자료형으로 파라미터 한 번에 받기

### @RequestParam Map

폼 전송 값을 한 번에 받고 싶을 때:

```java
@PostMapping("/add")
String writePost(@RequestParam Map<String, Object> formData) {
  System.out.println(formData);
  return "redirect:/list";
}
```

> 단, Map으로 받으면 타입 변환/검증이 번거로울 수 있음 (ex. price가 String으로 들어옴)
>

---

### Map 생성/사용

```java
Map<String, Object> test = new HashMap<>();
test.put("title", "모자");

Object title = test.get("title");
```

---

## 19) 가장 쉬운 저장: @ModelAttribute로 Entity/DTO 바인딩 후 save

폼 필드명이 엔티티 필드명과 같으면 자동 바인딩됨:

```java
@PostMapping("/add")
String writePost(@ModelAttribute Item item) {
  itemRepository.save(item);
  return "redirect:/list";
}
```

✅ 전제 조건

- `Item`에 `title`, `price` 필드가 있고
- setter/getter가 있거나 Lombok(`@Getter`, `@Setter`)이 적용되어 있어야 함
- HTML input의 `name`이 필드명과 일치해야 함 (`title`, `price`)

---

## 20) Thymeleaf로 HTML UI 재사용 (Fragment)

### Fragment 정의 (nav.html 등)

```html
<div class="nav" th:fragment="navbar">
  <a class="logo">SpringMall</a>
  <a href="/list">List</a>
  <a href="/write">Write</a>
</div>
```

- `th:fragment="navbar"`로 정의해두면 다른 파일에서 가져다 쓸 수 있음

### Fragment 사용

```html
<div th:replace="~{nav.html :: navbar}"></div>
```

> nav.html 파일은 보통 templates/ 아래에 둔다.
>
>
> (예: `src/main/resources/templates/nav.html`)
>

## 21) URL 파라미터(Path Variable)로 상세 페이지 만들기

### Path Variable 기본 문법

```java
@GetMapping("/detail/{id}")
String detail(@PathVariable Long id) {
  Optional<Item> result = itemRepository.findById(id);
  return "detail"; // templates/detail.html
}
```

- `/detail/1`, `/detail/2` 처럼 **URL 경로에 포함된 값(id)** 을 받아올 때 `@PathVariable` 사용
- `findById(id)` : 해당 `id`의 데이터를 조회

---

### Optional 타입 개념

`findById()`의 반환값은 `Optional<Item>` 인 경우가 많음.

- `Optional`은 **값이 있을 수도 있고 없을 수도 있음(null 가능성)** 을 감싸는 타입
- 값이 없는데 `.get()`을 호출하면 예외가 발생할 수 있음 (`NoSuchElementException`)
- 따라서 아래처럼 **존재 여부 체크** 후 꺼내는 게 안전함

```java
Optional<Item> result = itemRepository.findById(id);

if (result.isPresent()) {
  Item item = result.get();
  System.out.println(item);
}
```

- `result.isPresent()` : 값이 존재하는지 체크
- `result.get()` : Optional 안의 실제 값을 꺼냄 (존재할 때만)

---

## 22) Thymeleaf로 링크 만들기 (th:href)

Thymeleaf에서는 `href`를 직접 문자열로 쓰기보다 `th:href`로 URL을 생성하는 경우가 많다.

### 기본 링크

```html
<a th:href="@{/list}">List</a>
<a th:href="@{/write}">Write</a>
```

### Path Variable 포함 링크

```html
<a th:href="@{/detail/{id}(id=${item.id})}">상세보기</a>
```

- `/detail/{id}`형태의 URL에 `item.id` 값을 넣어 `/detail/1` 같은 링크를 만든다.

> 참고: th:href="@{...}" 형태를 사용한다. (@{}가 URL 표현식)
>

---

## 23) 예외 상황 처리하기 (error.html)

스프링 부트는 기본 에러 페이지를 제공하지만, `error.html`을 만들면 커스텀 에러 화면을 보여줄 수 있다.

### error.html 생성 위치

- `src/main/resources/templates/error.html`

### error.html 예시 (Thymeleaf)

```html
<!doctype html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>Error</title>
</head>
<body>
  <h1>에러가 발생했습니다</h1>

  <p th:text="${status}"></p>
  <p th:text="${error}"></p>
  <p th:text="${path}"></p>
  <p th:text="${message}"></p>
  <p th:text="${exception}"></p>
</body>
</html>
```

- `${status}`: HTTP 상태 코드 (예: 404, 500)
- `${error}`: 에러 이름
- `${path}`: 요청 경로
- `${message}`: 에러 메시지
- `${exception}`: 예외 클래스 정보

---

## 24) REST API 예외 처리

HTML을 반환하는 컨트롤러와 달리, REST API는 보통 **JSON/문자열 + HTTP 상태 코드**를 함께 내려주는 방식으로 예외를 처리한다.

### 24-1) try-catch로 직접 처리

`try` 안의 코드에서 에러가 나면 `catch`가 실행된다.

```java
@GetMapping("/api/test")
@ResponseBody
Stringtest() {
try {
thrownewException("에러임");
  }catch (Exception e) {
    System.out.println(e.getMessage());
return"에러남";
  }
}
```

---

### 24-2) throw Exception + throws Exception (Checked Exception)

함수 안에서 `throw new Exception(...)`처럼 **Checked Exception**을 던졌는데,

`try-catch`로 처리하지 않으면 메서드 선언부에 `throws Exception`을 붙여야 한다.

```java
@GetMapping("/api/detail/{id}")
@ResponseBody
Stringdetail()throws Exception {
thrownewException("이런저런에러");
}
```

> 참고: RuntimeException 계열은 throws를 강제하지 않는 경우가 많다.
>

---

### 24-3) ResponseEntity로 상태코드 + 메시지 함께 반환

에러 응답에 **HTTP 상태 코드**를 명확하게 실어 보내면 디버깅/원인 파악이 쉬워진다.

```java
@GetMapping("/api/detail/{id}")
ResponseEntity<String>detail() {
try {
thrownewException("이런저런에러");
  }catch (Exception e) {
return ResponseEntity
        .status(400)// 원하는 상태코드
        .body("에러이유: " + e.getMessage());
  }
}
```

---

### 24-4) @ExceptionHandler (컨트롤러 단위 예외 처리)

특정 컨트롤러 클래스 내부에서 발생한 예외를 한 곳으로 모아서 처리할 수 있다.
컨트롤러의 API 메서드들과 **나란히** 작성한다.

```java
@Controller
publicclassItemController {

// API들...

@ExceptionHandler(Exception.class)
public ResponseEntity<String>exceptionHandler(Exception e) {
return ResponseEntity
        .status(400)
        .body("ItemController 에러: " + e.getMessage());
  }
}
```

- `@ExceptionHandler(Exception.class)` : 해당 타입의 예외가 발생하면 이 메서드가 처리
- 파라미터로 예외 객체(`Exception e`)를 받을 수 있음

특정 에러만 처리하고 싶다면 타입을 바꾸면 된다.

```java
@ExceptionHandler(MethodArgumentTypeMismatchException.class)
public ResponseEntity<String>typeMismatch(MethodArgumentTypeMismatchException e) {
return ResponseEntity.status(400).body("파라미터 타입이 올바르지 않습니다.");
}
```

---

### 24-5) @ControllerAdvice (전역 예외 처리)

여러 컨트롤러에서 발생하는 예외를 **한 번에** 처리하고 싶을 때 사용한다.

```java
@ControllerAdvice
publicclassMyExceptionHandler {

@ExceptionHandler(Exception.class)
public ResponseEntity<String>handler(Exception e) {
return ResponseEntity
        .status(400)
        .body("모든 컨트롤러 에러시 발동: " + e.getMessage());
  }
}
```

- `@ControllerAdvice` : 전역(여러 컨트롤러) 예외 처리 클래스
- 컨트롤러별로 중복되던 try-catch/에러 응답 로직을 한 곳으로 모을 수 있음

---

## 25) Service 레이어 분리 (비즈니스 로직 분리)

컨트롤러(Controller)는 **요청/응답 처리**에 집중하고,

실제 “저장/검증/계산” 같은 **비즈니스 로직은 Service 레이어로 분리**하는 것이 좋다.

> 원칙: 하나의 함수 안에는 하나의 기능만 담는 게 좋음
>

---

### 25-1) Service 클래스 만들기

- 함수(비즈니스 로직)를 담을 클래스에 `@Service`를 붙인다.
- 보통 생성자 주입을 위해 `@RequiredArgsConstructor`를 함께 사용한다.

```java
@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;

  public void saveItem(String title, Integer price) {
    Item item = new Item();
    item.setTitle(title);
    item.setPrice(price);

    itemRepository.save(item);
  }
}
```

---

### 25-2) Controller에서 Service 사용하기

- Service를 사용하고 싶은 곳(Controller)에 `private final`로 등록한다.
- `@RequiredArgsConstructor`가 필요하다.

```java
@Controller
@RequiredArgsConstructor
public class ItemController {

private final ItemService itemService;

@PostMapping("/add")
  StringwritePost(String title, Integer price) {
    itemService.saveItem(title, price);
return"redirect:/list";
  }
}
```

---

## Dependency Injection (DI)

다른 클래스의 기능을 쓸 때 `new 클래스()`를 매번 호출하는 대신,

스프링이 **미리 만들어둔 객체를 주입받아 사용**하는 방식을 DI(의존성 주입)라고 한다.

### DI를 쓰는 이유

1. 매번 객체를 새로 만들지 않아도 되어 **효율적** (중복 생성 방지)
2. 클래스 간 결합도를 낮춰 **유지보수/테스트가 쉬움**

---

## Container / Bean 용어

- **Container (IoC Container)**: 스프링이 객체를 생성해서 보관/관리하는 공간
- **Bean**: 컨테이너가 만들어서 관리하는 객체

즉, `@Controller`, `@Service`, `@Repository` 등이 붙은 클래스들은 스프링이 Bean으로 만들어 컨테이너에 보관하고 필요할 때 주입해준다.

---

## 25-3) Service 레이어 예외 처리 방법

서비스에서 예외 상황(검증 실패, 데이터 없음 등)을 처리하는 방식은 크게 2가지.

### 방법 A) 실패 메시지를 return (단순한 경우)

```java
public StringsaveItem(...) {
if (price <0)return"가격은 음수일 수 없음";
  ...
return"ok";
}
```

- 장점: 단순함
- 단점: 호출하는 쪽에서 문자열 비교 등 처리가 번거로울 수 있음

### 방법 B) 예외를 발생시키기 (추천)

```java
publicvoidsaveItem(String title, Integer price) {
if (price <0)thrownewIllegalArgumentException("가격은 음수 불가");
  ...
}
```

- Thymeleaf(SSR) 화면: 예외가 터지면 `error.html` 등으로 이동 가능
- REST API: `@ExceptionHandler` / `@ControllerAdvice`에서 잡아서 상태코드+메시지로 응답 가능

---

## Exception 종류는 여러 가지

- `IllegalArgumentException` (잘못된 입력값)
- `NullPointerException` (null 접근)
- `MethodArgumentTypeMismatchException` (요청 파라미터 타입 불일치)
- 등등…

### 상태 코드를 명확하게 주고 싶다면: ResponseStatusException

```java
thrownewResponseStatusException(HttpStatus.NOT_FOUND,"상품이 존재하지 않습니다.");
```

- 원하는 HTTP 상태코드(404/400 등)와 메시지를 함께 설정할 수 있음
- REST API에서 특히 유용함