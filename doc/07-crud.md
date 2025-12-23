# 07) CRUD 구현

## Create: 글 작성 페이지 + 저장

### 글 작성 페이지
```java
@GetMapping("/write")
String write() {
  return "write"; // templates/write.html
}
```
### write.html

```html
<form action="/add" method="post">
<input name="title" />
<input name="price" />
<button type="submit">등록</button>
</form>
```

### @RequestParam으로 받기

```java
@PostMapping("/add")
String addPost(@RequestParam String title,@RequestParam Integer price) {
    return "redirect:/list";
}
```

### @ModelAttribute로 바로 바인딩 + save

```java
@PostMapping("/add")
String writePost(@ModelAttribute Item item) {
    itemRepository.save(item);
    return"redirect:/list";
}
```

---

## Read: 상세 페이지(PathVariable)

```java
@GetMapping("/detail/{id}")
String detail(@PathVariable Long id) { 
    Optional<Item> result = itemRepository.findById(id);
    return"detail";
}
```

### Optional 안전 처리

```java
Optional<Item> result = itemRepository.findById(id);
if (result.isPresent()) {
Itemitem= result.get();
}
```

---

## Update: JPA 수정(save로 덮어쓰기)

```java
@PostMapping("/edit")
String editItem(Long id, String title, Integer price) {
    Item item=newItem();
    item.setId(id);
    item.setTitle(title);
    item.setPrice(price);
    itemRepository.save(item);
    return"redirect:/list";
}
```

---

## Delete: AJAX + DELETE

### 화면(Thymeleaf)에서 호출

```html
<span onclick="fetch('/item?id=[[${i.id}]]', { method: 'DELETE' })">🗑️</span>

```

### Controller

```java
@DeleteMapping("/item")
ResponseEntity<String> deleteItem(@RequestParam Long id) {
    itemRepository.deleteById(id);
    return ResponseEntity.status(200).body("삭제완료");
}
```