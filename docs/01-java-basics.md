# 01) Java 기초 문법

## 타입
- `String`
- `int` / `Integer`
- `long` / `Long`
- `float` / `Float`
- `double` / `Double`
- `boolean` / `Boolean`

### primitive vs wrapper
- `int, long, boolean` → primitive(기본형)
- `Integer, Long, Boolean` → wrapper(객체형)
- DB/JPA에서는 보통 **wrapper 선호** (null 허용 등)

## var (Java 10+)
- 컴파일 시 타입 추론(동적 타입 아님)
- 지역 변수에서만 사용 가능

## final
- 재할당 불가
- “변경되면 안 되는 값”을 명확히 표현

---

## 클래스 / 객체 / 생성자

### class / object
- class: field(변수) + method(함수) 묶음
- object(인스턴스): `new`로 만든 실제 값

```java
class Test {
  String name;
}

Test test = new Test();
test.name = "bom";
```
### constructor(생성자)

- 객체 만들 때 자동 실행되는 초기화 함수
- 클래스명과 동일한 이름

```java
class Car {
String model="hi";
int price;

  Car(int price) {
this.price = price;// this.price = 이 객체의 price
  }
}

Car car1 = new Car(300000);
```
---

## 접근 제어자 / static

- `public`: 어디서나 접근
- (default / package-private): 같은 패키지에서만
- `private`: 클래스 내부에서만
- `protected`: 같은 패키지 + 상속받은 클래스

### static

- 객체 없이 클래스명으로 바로 사용

```java
Math.max(1,2);
```

---

## Getter/Setter 쓰는 이유

검증 로직을 넣을 수 있음

```java
public void setPrice(Integer price) {
if (price <0)throw new IllegalArgumentException("가격은 음수 불가");
this.price = price;
}
```