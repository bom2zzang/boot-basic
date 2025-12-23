# 04) JPA / Hibernate / Repository / DI

## 개념
- ORM: 객체로 DB를 다루는 방식
- JPA: ORM 표준 스펙
- Hibernate: JPA 구현체

## 의존성
```gradle
runtimeOnly 'com.mysql:mysql-connector-j'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
```
## application.properties

```
spring.datasource.url=jdbc:mysql://주소/DB이름
spring.datasource.username=아이디
spring.datasource.password=비번

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.show_sql=true
```

### ddl-auto 옵션

- `create`: 매번 새로 생성(데이터 삭제)
- `update`: 있으면 유지 + 변경 반영
- `validate`: 검증만
- `none`: 아무것도 안 함

## Entity 만들기

```java
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private Integer price;
}

```

### 컬럼 제약

```java
@Column(nullable = false, unique = true)
private String title;

```

## Repository

```java
public interface ItemRepository extends JpaRepository<Item, Long> {
}
```

## 조회

```java
List<Item> items = itemRepository.findAll();
```

## DI / 생성자 주입

```java
@RequiredArgsConstructor
@Controller
public class ItemController {
private final ItemRepository itemRepository;
}
```

## Lombok

```
compileOnly 'org.projectlombok:lombok'
annotationProcessor 'org.projectlombok:lombok'
```

자주 쓰는 어노테이션: `@Getter`, `@Setter`, `@ToString`, `@RequiredArgsConstructor`
