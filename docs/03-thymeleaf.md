# 03) Thymeleaf

## 의존성
```gradle
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
```
## Model 전달

```java
@GetMapping("/list")
String list(Model model) {
    model.addAttribute("name","홍길동");
    return "list";
}
```

```html
<h4 th:text="${name}"></h4>
```

## 반복문 (th:each)

```html
<div th:each="item : ${items}">
<h4 th:text="${item.title}"></h4>
</div>
```

> ${items}는 model.addAttribute("items", result)로 넣어야 함
>

## 링크 만들기 (th:href)

```html
<a th:href="@{/list}">List</a>
<a th:href="@{/write}">Write</a>
<a th:href="@{/detail/{id}(id=${item.id})}">상세</a>
```

## UI 재사용(Fragment)

### 정의 (nav.html)

```html
<div class="nav"th:fragment="navbar">
<a class="logo">SpringMall</a>
<a href="/list">List</a>
<a href="/write">Write</a>
</div>
```

### 사용

```html
<div th:replace="~{nav.html :: navbar}"></div>
```