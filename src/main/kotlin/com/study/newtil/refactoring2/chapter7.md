## 7장 캡슐화

### 7.2 컬렉션 캡슐화하기

객체지향에서 컬렉션을 캡슐화 할 때 주의점

- 컬렉션 변수로 캡슐화해도 getter에서 컬렉션을 그대로 반환하게 된다면, 컬렉션을 감싼 클래스가 아닌 사용하는 쪽에서 컬렉션 수정이 가능해진다.
- 즉 감싼 클래스도 모르게 컬렉션 원소들이 바뀔 수 있는 문제점이 있다.
    - 글쓴이는 컬렉션을 감싼 클래스에 `add()`, `remove()` 함수를 넣어 컬렉션 변경점을 감싼 클래스만 갖도록 해결한다고 함
- 또는 컬렉션 getter가 원본 컬렉션을 반환하지 않게 만드는 방법도 있다.
    - 읽기전용 컬렉션으로 반환하거나, 복사본으로 반환
        - 어느 방법으로 해도 무관하나 일관성을 위해 하나만 사용하는걸 추천한다.

### 7.3 기본형을 객체로 바꾸기

기본형을 객체로 바꾸는 순간

- 단순한 출력 이상의 기능이 필요해지는 순간 그 데이터를 표현하는 전용클래스를 정의한다고 한다.

### 7.4 임시 변수를 질의 함수로 바꾸기

함수 안에서 어떤 코드듸 결괏값을 뒤에서 다시 참조할 목적으로 임시 변수를 사용한다.

변수들을 각각의 함수로 만들면 일이 수월해진다. 추출한 함수에 변수를 따로 전달할 필요가 없어지기 때문이다.

변수 대신 함수로 만들어두면 비슷한 계산을 수행하는 다른 함수에서도 사용할 수 있어 코드 중복이 줄어든다.

- 여러곳에서 똑같은 방식으로 계산되는 변수를 발견할 때마다 함수로 바꿀 수 있는지 살펴본다.

```kotlin
// as-is
data class Item(
    val price: Long,
    val name: String
)

class Order(
    var quantity: Int,
    var item: Item,
) {
    private val price: Double
        get() {
            val basePrice = quantity * item.price
            var discountFactor = 0.98

            if (basePrice > 1000) discountFactor -= 0.03
            return basePrice * discountFactor
        }
}

// to-be
class Order(
    quantity: Int,
    item: Item,
) {
    val price: Double
        get() {
            return basePrice * discountFactor
        }

    private val basePrice: Long = quantity * item.price

    private val discountFactor: Double
        get() {
            // 변수에 값을 한 번 대입 한 뒤
            var discountFactor = 0.98
            // 여러차례 다시 대입하는 경우는 모두 질의 함수로 추출
            if (basePrice > 1000) discountFactor -= 0.03
            return discountFactor
        }
}
```

임시 변수를 질의함수로 다 바꿔야 하는 건 아니다.

- 변수를 사용할 때마다 계산 로직이 매번 다른 결과를 내는 경우는 질의함수로 바꿀 수 없다. (ex 스냅샷 용도)

### 7.5 클래스 추출하기

클래스로 추출하는 기준

- 일부 데이터와 메서드를 따로 정의할 수 있는 경우
- 함께 변경되는 일이 많거나 서로 의존하는 데이터들이 있는 경우

### 7.6 클래스 인라인하기

`7.5 클래스 추출하기`에 반대되는 행위이다. 글쓴이는 제 역할을 못해서 그대로 두면 안되는 클래스를 인라인한다고 한다.

- 클래스 추출 후 특정 클래스에 남은 역할이 거의 없을 때 이런 현상이 자주 발생한다.

두 클래스의 기능을 지금과 다르게 배분하고 싶을 때 클래스를 인라인 후 다시 추출하는 방법으로 사용된다.

