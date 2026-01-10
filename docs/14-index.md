# 14) Index 정리

## Index란?

테이블에서 특정 값(숫자/문자)을 **더 빠르게 찾기 위해** 사용하는 자료구조

- 인덱스는 특정 컬럼의 값을 **별도의 구조로 저장**해 두고(보통 B-Tree),

  이를 통해 DB가 데이터를 더 빠르게 탐색할 수 있게 함

- 컬럼이 숫자뿐 아니라 **문자(String)** 여도 인덱스를 만들 수 있음
- 인덱스는 생성해 두면, DB 옵티마이저가 쿼리 실행 시 **필요하다고 판단될 때 자동으로 사용**

  (항상 쓰는 건 아니고, 비용 기반으로 선택함)


---

## Index의 단점

1. **용량 증가**

   인덱스는 별도 구조로 저장되므로 인덱스를 추가할수록 DB 저장 용량이 늘어남

2. **쓰기 성능 저하 가능성**

   원본 테이블에 INSERT/UPDATE/DELETE가 발생할 때마다 인덱스도 함께 갱신되어야 하므로

   데이터 변경이 많은 테이블에서는 성능이 느려질 수 있음


---

## JPA에서 Index 만들기

```java
@Entity
@Table(indexes = @Index(columnList = "title", name = "idx_title"))
publicclassItem {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false)
private String title;
}
```

---

## 성능 확인 방법

### 1) Hibernate 통계 활성화

```
# application.properties
spring.jpa.properties.hibernate.generate_statistics=true
```

이 설정을 켜면 Hibernate가 쿼리 수행 통계(시간 등)를 로그로 남긴다.

### 2) EXPLAIN으로 인덱스 사용 여부 확인

DB에서 쿼리 앞에 `EXPLAIN`을 붙이면 **실행 계획**을 확인할 수 있다.

- 인덱스를 타는지(사용하는지)
- 풀스캔인지
- 어떤 인덱스를 사용하는지

  등을 볼 수 있다.


```sql
EXPLAIN SELECT*FROM itemWHERE title='모자';
```

---

# Full Text Index 정리

## Full Text Index란?

문장(텍스트) 컬럼에서 **단어를 추출**한 뒤, 해당 단어들을 기반으로 검색을 빠르게 해주는 인덱스다.

- `MATCH(...) AGAINST(...)` 문법으로 검색한다.
- 일반 인덱스(B-Tree)와는 목적과 동작 방식이 다르다.
- 검색 결과는 보통 **관련도(relevance)** 점수 기반으로 정렬되어 상위 결과가 먼저 나온다.

---

## 한국어/중국어/일본어에서의 이슈

영어처럼 “공백 기준 단어 분리”가 잘 되는 언어가 아니라서, 단어 추출 방식이 제대로 동작하지 않거나

짧은 검색어/부분 검색이 기대처럼 안 될 수 있다.

---

## n-gram parser

한글처럼 형태소 분리가 어려운 언어는 `ngram` 파서를 써서:

- 문장을 **n글자 단위로 쪼개서** 인덱스를 만든다.
- 예: “모자” → “모”, “자” (2-gram이면 “모자” 단위 등)

---

## Full Text Index 생성 (ngram)

```sql
CREATE FULLTEXT INDEX 인덱스이름
ON 테이블명(컬럼명)WITH PARSER ngram;
```

---

# JPA에서 Native SQL 사용하기

## 기본 형태

```java
@Query(value = "SQL문", nativeQuery = true)
리턴타입 함수명(...);
```

## 파라미터 사용

`?1`, `?2`, `?3` ... 형태로 사용한다.

```java
@Query(value = "SELECT * FROM shop.item WHERE id = ?1", nativeQuery = true)
Item test1(int id);
```

---

# Full Text 검색하기

## MATCH / AGAINST 문법

```sql
MATCH(컬럼명) AGAINST('검색어')
```

## JPA 예시

```java
@Query(value = "SELECT * FROM shop.item WHERE MATCH(title) AGAINST(?1)", nativeQuery = true)
List<Item>fullTextSearch(String text);
```

- Full Text Index로 검색하면 DB가 관련도가 높다고 판단한 결과가 먼저 나온다.

---