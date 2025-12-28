# 06) Service 레이어 분리

컨트롤러는 요청/응답에 집중하고, 비즈니스 로직은 Service로 분리한다.

## Service 만들기
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
## Controller에서 Service 사용

```java
@Controller
@RequiredArgsConstructor
public class ItemController {
private final ItemService itemService;

@PostMapping("/add")
  String writePost(String title, Integer price) {
    itemService.saveItem(title, price);
    return"redirect:/list";
  }
}
```

## Service 예외 처리 방식
- return으로 실패 메시지 전달(단순)
- 예외 throw(추천) → `@ExceptionHandler/@ControllerAdvice`로 처리 가능