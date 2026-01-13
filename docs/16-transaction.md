# 16) Transaction (트랜잭션)

트랜잭션은 여러 DB 작업을 **하나의 작업 단위**로 묶어서 처리하는 개념이다.  
중간에 하나라도 실패하면 **전체를 취소(ROLLBACK)** 하고, 모두 성공하면 **전체를 반영(COMMIT)** 한다.

---

## SQL 트랜잭션 기본 문법

```sql
START TRANSACTION;

-- 삽입/수정/삭제 명령 1;
-- 삽입/수정/삭제 명령 2;
-- 삽입/수정/삭제 명령 3;

-- 에러가 발생하면
ROLLBACK;

-- 에러가 없으면
COMMIT;
```


---

## JPA `@Transactional`

Spring에서는 `@Transactional`로 트랜잭션을 관리할 수 있음

- 보통 **메서드 단위**로 붙이는 것이 가장 명확함
- 클래스에 붙일 수도 있지만, 어떤 메서드가 트랜잭션인지 한눈에 보기 어려울 수 있음
- 일반적으로 **public 메서드**에 붙여 사용하는 경우가 많음


---

## 테스트에서 `@Transactional` 활용

테스트 코드에 `@Transactional`을 붙이면, 테스트가 끝난 뒤 **자동으로 롤백**되어 DB가 깨끗하게 유지됨
(테스트 데이터가 DB에 남지 않도록 할 때 유용)
```
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testOrderCreation() {
        Order order = new Order();
        order.setCustomerName("");
        order.setTotalAmount(10);

        orderRepository.save(order);

        assertEquals(1, orderRepository.count());
    }
}
```