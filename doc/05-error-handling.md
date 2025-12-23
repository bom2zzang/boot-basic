# 05) 예외 처리

## error.html 커스텀
위치: `src/main/resources/templates/error.html`

```html
<!doctype html>
<html lang="ko">
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
- `${status}`: HTTP 상태 코드
- `${error}`: 에러 이름
- `${path}`: 요청 경로
- `${message}`: 메시지
- `${exception}`: 예외 클래스

---

## REST API 예외 처리

### 1) try-catch

```java
@GetMapping("/api/test")
@ResponseBody
String test() {
    try {
        throw new Exception("에러임");
    }catch (Exception e) {
        return"에러남";
    }
}
```

### 2) throws Exception (checked exception)

```java
@GetMapping("/api/detail/{id}")
@ResponseBody
String detail() throws Exception {
    throw new Exception("이런저런에러");
}
```

### 3) ResponseEntity로 상태코드 포함

```java
@GetMapping("/api/detail/{id}")
ResponseEntity<String>detail() {
    try {
        throw new Exception("이런저런에러");
    }catch (Exception e) {
        return ResponseEntity.status(400).body("에러이유: " + e.getMessage());
    }
}
```

### 4) @ExceptionHandler (컨트롤러 단위)

```java
@ExceptionHandler(Exception.class)
public ResponseEntity<String> handler(Exception e) {
    return ResponseEntity.status(400).body("에러: " + e.getMessage());
}
```

### 5) @ControllerAdvice (전역)

```java
@ControllerAdvice
public class MyExceptionHandler {
@ExceptionHandler(Exception.class)
public ResponseEntity<String> handler(Exception e) {
    return ResponseEntity.status(400).body("전역 처리: " + e.getMessage());
  }
}
```