## **1) IntelliJ / 실행 단축키**

- `sout` + Enter : `System.out.println()`
- Run: `Ctrl + R`(Mac은 `Control + R` 또는 상단 Run)
- Rebuild/Compile: `⌘ + F9`
- Gradle 재동기화(중요): Gradle 탭 → Reload / 또는 `./gradlew build`

---

## 2) Java 기초 문법 (정리)

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

### Dependency Injection (DI)

다른 클래스의 기능을 쓸 때 `new 클래스()`를 매번 호출하는 대신,

스프링이 **미리 만들어둔 객체를 주입받아 사용**하는 방식을 DI(의존성 주입)라고 한다.

### DI를 쓰는 이유

1. 매번 객체를 새로 만들지 않아도 되어 **효율적** (중복 생성 방지)
2. 클래스 간 결합도를 낮춰 **유지보수/테스트가 쉬움**

---

### Container / Bean 용어

- **Container (IoC Container)**: 스프링이 객체를 생성해서 보관/관리하는 공간
- **Bean**: 컨테이너가 만들어서 관리하는 객체

즉, `@Controller`, `@Service`, `@Repository` 등이 붙은 클래스들은 스프링이 Bean으로 만들어 컨테이너에 보관하고 필요할 때 주입해준다.

---

### 25-3) Service 레이어 예외 처리 방법

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

### Exception 종류는 여러 가지

- `IllegalArgumentException` (잘못된 입력값)
- `NullPointerException` (null 접근)
- `MethodArgumentTypeMismatchException` (요청 파라미터 타입 불일치)
- 등등…

### 상태 코드를 명확하게 주고 싶다면: ResponseStatusException

```java
throw new ResponseStatusException(HttpStatus.NOT_FOUND,"상품이 존재하지 않습니다.");
```

- 원하는 HTTP 상태코드(404/400 등)와 메시지를 함께 설정할 수 있음
- REST API에서 특히 유용함

---

## 26) 수정/삭제 기능 추가 (Edit / Delete)

---

### 26-1) JPA 수정 기능 (`save()`로 덮어쓰기)

JPA는 **이미 존재하는 id**를 가진 엔티티를 `save()` 하면 **INSERT가 아니라 UPDATE(수정)** 처리가 된다.

```java
@PostMapping("/edit")
String editItem(Long id, String title, Integer price) {
  Item item = new Item();
  item.setId(id);       // 기존에 존재하는 id
  item.setTitle(title);
  item.setPrice(price);

  itemRepository.save(item); // id가 있으면 덮어쓰기(수정)
  return "redirect:/list";
}
```

✅ 포인트

- `id`가 **DB에 존재하면 수정**, 존재하지 않으면 **새로 추가**될 수 있음
- 폼 전송 방식이면 `redirect:/list`로 이동 가능
- (실무에선 보통 `findById → 값 변경 → save`로 수정하는 방식도 많이 사용)

---

### 26-2) AJAX 삭제 기능 (fetch + DELETE)

Thymeleaf에서 id를 끼워 넣어서 DELETE 요청을 보낼 수 있다.

```html
<span onclick="fetch('/item?id=[[${i.id}]]', { method: 'DELETE' })">🗑️</span>
```

> 삭제 후 목록 갱신이 필요하면 .then(() => location.reload()) 또는 location.href='/list' 같은 처리를 추가한다.
>

---

### 26-3) 삭제 API (Controller)

```java
@DeleteMapping("/item")
ResponseEntity<String> deleteItem(@RequestParam Long id) {
  itemRepository.deleteById(id);
return ResponseEntity.status(200).body("삭제완료");
}
```

- `@DeleteMapping` : HTTP DELETE 요청 처리
- `@RequestParam Long id` : `/item?id=1` 형태로 전달된 id 받기
- `deleteById(id)` : 해당 id 데이터 삭제
- `ResponseEntity` : 상태 코드 + 메시지를 함께 반환 가능

---

## 27) Session / JWT / OAuth 개념 정리

로그인이 필요한 기능(마이페이지, 글쓰기, 결제 등)을 만들 때는 “이 요청을 보낸 사람이 누구인지”를 확인해야 한다.

이때 대표적으로 **Session 방식**, **JWT(Token) 방식**을 사용하고, 외부 서비스 로그인(구글/카카오 등)은 **OAuth**를 사용한다.

---

### 27-1) Session 방식

### 로그인 흐름

1. 유저가 로그인하면 서버(또는 DB/저장소)에 아래 정보를 저장한다.
  - 유저 아이디
  - 유효기간(만료 시간)
  - `session id` (랜덤 문자열/숫자)
2. 유저에게 “입장권(쿠키)”을 발급할 때는 보통 **session id만** 담아서 보낸다.

> session id는 “세션 레코드를 구분하는 랜덤 키”라고 보면 됨.
>

### 로그인 필요한 요청 흐름

1. 유저가 GET/POST 요청 시 **쿠키(입장권)** 를 함께 제출한다.
2. 서버는 쿠키의 `session id`로 저장소(DB/Redis 등)를 조회한다.
3. 세션이 유효하면 요청을 통과시킨다.

### 장점

- 매 요청마다 저장소를 확인하므로 **요청 단위로 엄격한 인증/만료/차단 관리**가 쉽다.
- 서버가 세션을 지우면 강제 로그아웃(토큰 폐기)이 가능하다.

### 단점

- 매 요청마다 조회하니 **저장소 부하**가 커질 수 있다.
- 트래픽이 크면 **Redis 같은 빠른 저장소**로 세션을 관리하는 경우가 많다.

---

### 27-2) JWT(Token) 방식

> Token은 여러 종류가 있지만 보통 실무에서 말하는 token은 JWT(JSON Web Token) 를 의미하는 경우가 많다.
>

JWT는 “입장권(토큰)” 안에 유저 정보를 담고, 서버는 매 요청마다 DB를 조회하지 않고도 인증할 수 있게 한다.

### 로그인 흐름

1. 유저가 로그인하면 서버는 JWT를 만들어 유저에게 발급한다.
2. JWT 안에는 보통 다음 같은 정보(Claim)가 들어간다.
  - 유저 id (또는 username)
  - 발급 시간
  - 만료 시간(exp)
3. 서버는 (세션처럼) DB에 별도 저장을 안 하거나, 최소화하는 경우가 많다.

### 로그인 필요한 요청 흐름

1. 유저가 요청할 때 JWT를 함께 보낸다.
  - 예: `Authorization: Bearer <token>`
2. 서버는 JWT의 **서명(signature)** 을 검증하고
3. 만료 여부(exp) 등을 확인한 뒤 통과시킨다.
4. 유저 정보가 필요하면 토큰에 들어있는 값을 꺼내서 사용한다.

### 위조하면 어떡함?

- JWT는 단순히 암호화가 아니라, 보통 **서명(Signature)** 기반이다.
- 토큰 생성 시 **서버만 아는 secret(또는 개인키)** 로 서명을 만든다.
- 내용이 바뀌거나 secret이 다르면 서명이 맞지 않아서 **위조 여부를 검증으로 바로 잡아낼 수 있다.**

### 장점

- 매 요청마다 DB 조회가 필요 없어서 **확장성 좋고 부하가 적음**
- 서버가 여러 대인 환경(마이크로서비스/수평 확장)에 유리

### 단점

- 토큰이 유출되면 **만료 전까지 막기 어려움**
  (서버가 강제로 “이 토큰 무효”를 바로 적용하기 까다로움)
- 다른 기기에서 발급된 토큰을 일괄 로그아웃시키는 것도 어렵다.
- 블랙리스트(폐기 토큰 목록)를 저장소에 저장해서 체크할 수 있지만,
  그러면 매 요청마다 조회가 들어가서 세션 방식과 유사해진다.

---

### 27-3) OAuth (구글/카카오 로그인 등)

OAuth는 “입장권” 자체라기보다는,

> A 사이트(구글/카카오)의 사용 권한을 B 사이트(내 서비스)가 제한적으로 빌려오는 절차(규칙)
>

를 정의한 표준이다.

예)

- 내 서비스에서 “구글로 로그인” 버튼을 누르면
- 유저가 구글에서 동의하고
- 내 서비스는 구글로부터 “유저 정보 접근 권한(토큰)”을 받아
- 그 유저를 우리 서비스 회원으로 로그인 처리한다.

---

## 28) Spring Security (구현 도구)

인증/인가를 직접 구현하기보다 보통 **Spring Security**를 사용하면 편하다.

- Session 기반 로그인 기능 구현 가능
- JWT 기반 인증 구현 가능
- OAuth 로그인(구글/카카오) 연동 가능

---

### 28-1) 라이브러리 설치

```
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.1.RELEASE'
```

---

### 28-2) 기본 설정(SecurityConfig) 바꾸기

`SecurityConfig.java` 파일을 만들고 아래처럼 작성한다.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
public SecurityFilterChainfilterChain(HttpSecurity http)throws Exception {
    http.authorizeHttpRequests((authorize) ->
        authorize.requestMatchers("/**").permitAll()
    );
return http.build();
  }
}
```

- `SecurityFilterChain`:
  - 유저 요청(Request) ↔ 서버 응답(Response) 사이에서 자동으로 실행되는 보안 필터들의 묶음
  - 다른 프레임워크에선 “미들웨어” 같은 개념으로 이해해도 됨
- `.requestMatchers()` : URL 패턴 지정
- `/**` : 모든 URL
- `.permitAll()` : 로그인 여부와 관계없이 접근 허용

  → 위 설정은 **모든 URL을 모든 유저에게 허용**한다.


> 참고: 위 문법은 **Spring Security 6+**에서 사용되는 스타일이다.
>

---

### 28-3) CSRF 보안 기능 끄기 (학습/테스트용)

개발 초기에 폼/POST가 막히는 경우가 많아, 학습 단계에서 일단 꺼두기도 한다.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
public SecurityFilterChainfilterChain(HttpSecurity http)throws Exception {

    http.csrf((csrf) -> csrf.disable());

    http.authorizeHttpRequests((authorize) ->
        authorize.requestMatchers("/**").permitAll()
    );

return http.build();
  }
}
```

### CSRF란?

- 다른 사이트가 내 사이트에 **몰래 요청(특히 POST)** 을 보내서 공격하는 방식
- 예: 공격자가 내 사이트로 POST 요청을 보내는 `<form>`을 위조해서 유저가 클릭하게 만들면,

  유저의 로그인 쿠키를 이용해 서버에 요청이 날아갈 수 있음


### CSRF 방어 방식(개념)

- 폼을 내려줄 때 서버가 **랜덤 토큰**을 같이 내려줌 (숨겨진 input 등)
- 폼 전송 시 그 토큰도 함께 보내게 하고
- 서버는 토큰이 일치할 때만 요청을 허용

### JWT와 CSRF

- JWT를 쿠키가 아니라 `Authorization` 헤더에 담아 보내는 방식이면,

  브라우저가 자동으로 토큰을 실어 보내지 않기 때문에 CSRF 위험이 줄어드는 편이라

  CSRF를 끄는 구성도 종종 사용함

- 다만 운영 환경에서는 “현재 인증 방식(Session/JWT) + 저장 위치(cookie/header)”에 따라

  CSRF 설정을 다시 정확히 잡는 것이 좋다.


---

### 28-4) 패스워드는 해싱해서 저장해야 함

비밀번호는 DB에 **평문으로 저장하면 안 되고**, 반드시 해싱해서 저장해야 한다.

- 해싱 알고리즘 예: `bcrypt`, `argon2`, `scrypt`, `SHA-*` 등
- Spring Security는 `BCryptPasswordEncoder` 같은 구현체를 쉽게 사용할 수 있다.

```java
varencoder=newBCryptPasswordEncoder();
System.out.println(encoder.encode("qwer1234"));
```

---

### 28-5) PasswordEncoder를 Bean으로 등록해서 DI로 사용하기

매번 `new BCryptPasswordEncoder()`를 만들지 않고, 스프링이 객체를 한 번 만들어 관리(Bean)하도록 한 뒤

필요한 곳에서 DI로 주입받아 쓸 수 있다.

### Bean 등록

```java
@Configuration
@EnableWebSecurity
publicclassSecurityConfig {

@Bean
  PasswordEncoderpasswordEncoder() {
returnnewBCryptPasswordEncoder();
  }

// (filterChain 등 생략)
}
```

### 사용 (DI)

```java
@RequiredArgsConstructor
@Service
publicclassUserService {

privatefinal PasswordEncoder passwordEncoder;

publicvoidsignup(String rawPassword) {
Stringencoded= passwordEncoder.encode(rawPassword);
// encoded 값을 DB에 저장
  }
}
```

- `PasswordEncoder`는 “비밀번호를 안전하게 해싱/검증하는 인터페이스”
- 구현체로 `BCryptPasswordEncoder`를 사용하면 보통 무난하게 시작 가능

--- 
## 29) 로그인 구현 (Spring Security Form Login)

Spring Security의 **폼 로그인(formLogin)** 을 이용하면,
로그인/로그아웃/인증 체크를 비교적 빠르게 구현할 수 있다.

---

### 29-1) 로그인 설정 (SecurityConfig)

`SecurityConfig.java`의 `filterChain` 안에 `formLogin` 설정을 추가한다.

```java
http.formLogin((formLogin) -> formLogin
    .loginPage("/login")
    .defaultSuccessUrl("/")
    .failureUrl("/fail")
);
```

- `formLogin()` : “폼 로그인 방식으로 인증하겠다”는 설정
- `loginPage("/login")` : 로그인 페이지 URL (직접 만든 로그인 화면)
- `defaultSuccessUrl("/")` : 로그인 성공 시 이동할 URL
- `failureUrl("/fail")` : 로그인 실패 시 이동할 URL

---

### 29-2) MyUserDetailsService (왜 필요한가?)

Spring Security가 아이디/비번 검증을 하려면

**DB에 저장된 비밀번호(암호화된 값)** 를 알아야 한다.

근데 Security는 비밀번호가 DB 어디 테이블/어느 컬럼에 있는지를 모른다.

그래서 **username을 받으면 DB에서 유저를 찾아서 UserDetails로 돌려주는 코드**를 만들어줘야 한다.

그 역할을 하는 게 `UserDetailsService` 구현체이다.

### MyUserDetailsService 예시

```java
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

private final MemberRepository memberRepository;

@Override
public UserDetailsloadUserByUsername(String username)throws UsernameNotFoundException {

var result= memberRepository.findByUsername(username);

if (result.isEmpty()) {
throw new UsernameNotFoundException("그런 아이디 없음");
    }

varuser= result.get();

    List<GrantedAuthority> authorities =newArrayList<>();
    authorities.add(newSimpleGrantedAuthority("일반유저"));

return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),// DB에 저장된 암호화된 비번
        authorities
    );
  }
}
```

- `loadUserByUsername(username)`에서 **DB 조회**
- 조회한 유저의 `username / password(암호화된 값) / 권한목록`을 만들어서 `UserDetails`로 반환
- 그러면 Security가 **비밀번호 비교(검증) / 인증 처리**를 자동으로 진행해준다.

---

### 29-3) MemberRepository에 findByUsername 만들기

`findById()`는 `JpaRepository` 기본 메서드지만,

`findByUsername()`은 직접 선언해야 한다.

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member>findByUsername(String username);
}
```

- `findBy컬럼명(값)` 형태로 만들면, Spring Data JPA가 자동으로 쿼리를 만들어준다.
- “없을 수도 있음”을 고려해서 `Optional`이 흔히 사용된다.

---

### 29-4) 로그인 정보 확인 (Controller에서 Authentication 사용)

로그인한 유저 정보는 컨트롤러에서 `Authentication` 파라미터로 받을 수 있다.

```java
@GetMapping("/my-page")
public StringmyPage(Authentication auth) {

  System.out.println(auth);// 인증 객체 전체
  System.out.println(auth.getName());// 보통 username

  System.out.println(auth.isAuthenticated());// 로그인 여부

  System.out.println(
      auth.getAuthorities().contains(newSimpleGrantedAuthority("일반유저"))
  );// 특정 권한 보유 여부

return"mypage";
}
```

---

### 29-5) Thymeleaf에서 로그인 정보 출력 (Spring Security Extras)

`thymeleaf-extras-springsecurity6` 의존성이 있으면 템플릿에서 인증 정보를 쉽게 쓸 수 있다.

```html
<span sec:authentication="principal"></span>
<span sec:authentication="principal.username"></span>
<span sec:authentication="principal.authorities"></span>
```

권한/로그인 여부에 따른 조건 렌더링:

```html
<span sec:authorize="hasAuthority('일반유저')">특정권한이 있으면 보임</span>
<div sec:authorize="isAuthenticated()">
  로그인된 사람만 보임</div>
```

---

### 29-6) 로그아웃 설정 (SecurityConfig)

```java
http.logout(logout -> logout.logoutUrl("/logout"));
```

- `/logout`으로 요청하면 로그아웃 처리
- 기본적으로 세션 기반이면 세션이 종료된다.

---

### 29-7) 로그인 필요 여부를 어노테이션으로 체크 (@PreAuthorize)

특정 API에 “로그인/권한 필요” 조건을 걸 수 있다.

```java
@PreAuthorize("isAuthenticated()")
@GetMapping("/admin")
public Stringadmin() {
return"admin";
}
```

자주 쓰는 표현

- `@PreAuthorize("isAuthenticated()")` : 로그인한 사람만
- `@PreAuthorize("isAnonymous()")` : 로그인 안 한 사람만
- `@PreAuthorize("hasAuthority('어쩌구')")` : 특정 권한 가진 사람만

> 참고: @PreAuthorize를 쓰려면 보통 메서드 보안 활성화 설정이 추가로 필요할 수 있다.
>