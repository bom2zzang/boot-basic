# 11) Pagination (페이지네이션)

목록 데이터를 한 번에 다 불러오지 않고, **페이지 단위로 나눠서 조회**하는 방법.

---

## 11-1) Repository에 페이지 조회 메서드 만들기

```java
public interface ItemRepository extends JpaRepository<Item, Long> {
  Page<Item> findPageBy(Pageable page);
  // Page: 전체 개수(total)까지 포함
  // total이 필요 없으면 Slice도 사용 가능
}
```

- `Page<T>`: 조회 결과 + 전체 행 개수/전체 페이지 수 정보 포함
- `Slice<T>`: 다음 페이지가 있는지 여부만 제공 (count 쿼리 줄여서 가벼움)

---

## 11-2) Controller에서 페이지 단위로 조회하기

```java
@GetMapping("/list/page/{pageNo}")
String getListPage(Model model, @PathVariable Integer pageNo) {

  Page<Item> result = itemRepository.findPageBy(
      PageRequest.of(pageNo -1,5)
  );

  System.out.println(result.getTotalPages());// 총 페이지 수

  model.addAttribute("items", result);
return"list";// templates/list.html
}
```
- `PageRequest.of(page, size)`
    - `page`는 **0부터 시작**한다.
    - 그래서 URL이 `/page/1`부터 시작하면 `pageNo - 1`로 변환해줘야 함.
- `size = 5` → 한 페이지에 5개씩

---

## 11-3) Page 객체에서 자주 쓰는 값들

```java
result.getContent();// 실제 데이터 List<Item>
result.getTotalPages();// 전체 페이지 수
result.getTotalElements();// 전체 데이터 개수
result.getNumber();// 현재 페이지 번호(0-base)
result.getSize();// 페이지 크기
result.hasNext();// 다음 페이지 존재 여부
result.hasPrevious();// 이전 페이지 존재 여부
```

> Thymeleaf에서 목록을 반복 돌릴 때는 items.getContent()를 쓰는 방식이 편하다.

---

## 11-4) Thymeleaf 예시 (목록 출력)

컨트롤러에서 `model.addAttribute("items", result)`로 `Page<Item>` 자체를 넘겼다면:

```html
<div th:each="item : ${items.content}">
<h4 th:text="${item.title}"></h4>
<p th:text="${item.price}"></p>
</div>
```

---

## 11-5) 페이지 이동 링크 예시 (이전/다음)

```html
<a th:if="${items.hasPrevious()}"
th:href="@{/list/page/{p}(p=${items.number})}">
  이전
</a>

<a th:if="${items.hasNext()}"
th:href="@{/list/page/{p}(p=${items.number + 2})}">
  다음
</a>
```
- `items.number`는 0-base
- URL은 1-base(`/page/1`, `/page/2`)로 보이게 만들었으니:
    - 이전 페이지 URL: `(items.number)` → 예) 현재 2페이지(0-base=1)면 이전은 1페이지(1-base=1)
    - 다음 페이지 URL: `(items.number + 2)` → 예) 현재 1(0-base)면 다음은 3? 가 아니라

      1-base로 맞추려면 `+2`가 필요 (0-base + 1-base 보정)


> 페이지 번호를 0-base로 갈지 1-base로 갈지 프로젝트 초기에 통일해두면 링크 계산이 훨씬 쉬워진다.