# 15) JPA에서 JOIN과 연관관계 매핑하기

## 제2정규화(2NF) 정리

### 언제 컬럼을 분리(정규화)할까

- 어떤 컬럼이 **다른 테이블에 이미 존재**한다면, 이 테이블에서 **중복 저장하지 말고 분리**를 고려
- 이 테이블의 **핵심 주제와 관련이 약한 정보**라면, 별도 테이블로 빼는 것을 고려
- 단, **정확하지 않아도 되는 값(대충/캐시/표시용)** 이거나, 분리했을 때 조회 비용이 너무 커진다면 **굳이 빼지 않아도 됨**.

### 정규화 이후 데이터 조회 방식

- 정규화를 하면 데이터가 여러 테이블로 분리되므로,
    - **JOIN** 으로 한 번에 가져오거나
    - **DB 조회를 2번(또는 여러 번)** 해서 조합해서 사용

---

## JPA에서 JOIN(연관관계) 걸기

### FK 컬럼을 엔티티 연관관계로 표현하기 (`@ManyToOne`)

`Sales`가 `Member`를 참조하는 경우:

```java
@ManyToOne
@JoinColumn(
    name = "member_id",
    foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
)
private Member member;
// private Long memberId;  
```

- DB에는 `sales.member_id` 컬럼이 FK 역할
- JPA에서는 `memberId` 숫자 필드 대신 `Member member` 객체로 연결해 관리

---

## `@ManyToOne` FetchType

```java
@ManyToOne(fetch = FetchType.LAZY)// 필요할 때만 가져옴 (권장)
@ManyToOne(fetch = FetchType.EAGER)// 항상 가져옴
```

- **LAZY(지연로딩)**: Sales만 조회하고, member가 필요할 때 member를 별도로 조회
- **EAGER(즉시로딩)**: Sales를 조회할 때 member도 같이 조회하려고 함

  → 예측하기 어려운 추가 쿼리 유발 가능해서 보통 LAZY가 더 안전함


---

## `@ManyToOne`의 단점: N+1 문제

### N+1 문제

- Sales 목록을 N개 조회한 후,
- 각 Sales마다 member를 접근하면 member 조회 쿼리가 N번 더 나감

  → 결과적으로 **1 + N 번 쿼리**가 실행되는 문제


### 해결 1) SQL JOIN 직접 사용

```sql
SELECT *
FROM sales
INNER JOIN member ON sales.member_id= member.id;
```

---
## 해결 2) JPQL `JOIN FETCH` 사용 (JPA에서 가장 많이 씀)

### JPQL이란?
- SQL을 엔티티 중심으로 쓰기 쉽게 만든 문법
- **JPA에서만** 사용

```java
@Query("SELECT s FROM Sales s JOIN FETCH s.member")
List<Sales> customFindAll();
```

- `JOIN FETCH`는 연관 객체(member)까지 **한 번에 같이 로딩**해서 N+1을 예방함

---

## JOIN FETCH의 주의점: “모든 컬럼을 다 가져옴”

- 엔티티를 통째로 가져오므로 불필요한 컬럼도 함께 로딩될 수 있음

### 대응 방법

- **DTO로 매핑해서 필요한 필드만 반환**
    - 매핑 라이브러리 사용하거나
    - JPQL에서 `new DTO(...)`로 바로 DTO 생성

```java
@Query("SELECT new com.bom.shop.sales.SalesDto(s.id, m.username, s.price, s.count) " +
       "FROM Sales s JOIN s.member m")
List<SalesDto> findSalesDtos();
```

---

## 반대 방향 조회: `@OneToMany`

- `@ManyToOne`은 “Sales에서 Member를 같이 조회”가 가능
- `@OneToMany`는 반대로 “Member에서 그 회원의 Sales 목록을 보고 싶을 때” 사용

```java
public class Member {
    @OneToMany(mappedBy = "member")
    List<Sales> sales = new ArrayList<>();
}
```

- `mappedBy = "member"`는 **Sales 엔티티의 member 필드가 주인**이라는 뜻


---

## `@OneToMany` 상호참조(ToString) 오류

양방향 관계에서 `@ToString`을 그대로 쓰면:
- Member → Sales → Member → Sales … 무한 참조로 터질 수 있음

### 해결
```java
@ToString.Exclude
@OneToMany(mappedBy = "member")
List<Sales> sales = new ArrayList<>();
```