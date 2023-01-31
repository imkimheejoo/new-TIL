## Generics

### 제네릭 타입

```kotlin
public inline fun <T, R> T.let(block: (T) -> R): R  // T, R 이 제네릭 타입이다.
```

- 제네릭 타입 파라미터
    - `Map<K,V>`를 예로 들었을 때 K, V 가 제네릭 타입 파라미터이다.
    - `Map<String, Person>` 처럼 구체적인 타입을 타입인자로 넘겨서 사용이 가능하다.
- 코틀린 컴파일러도 보통 타입과 마찬가지로 타입인자 추론이 가능하다.

```kotlin
val names = listOf("Heejoo", "Heejun")  // String을 담는 List를 추론 가능
val emptyNames = emptyList<String>()    // <String>을 쓰지 않으면 어떤 타입을 담는 리스트인지 알 수 없다.  = 타입 추론이 불가능
```

### 제네릭은 언제 써야 할까?

> 특정클래스나 함수가 여러 타입을 받고 싶을 때 -> class AA<T>, class StringList: List<String>
>
> 여러 타입이 동일한 로직을 수행하고 싶을 때

```kotlin
// AS-IS
fun List<String>.convertValuesToString() = this.joinToString { it }
fun List<Int>.convertValuesToString() = this.joinToString { it }

// List<String>, List<Int> 에서 모두 동일한 로직을 사용할 때 타입을 제네릭으로 바꿔서 사용하자!

// TO-BE
fun <T> List<T>.convertValuesToString() = this.joinToString { it }
```

### 타입 파라미터 제약 = 타입인자를 제한

```kotlin
fun <T : Number> List<T>.sum(): T    // List<String>은 sum 함수 쓸 수 X 
```

타입 파라미터에 둘 이상을 제약해야할 때:  `where`

```kotlin
fun <T> copyWhenGreater(list: List<T>, threshold: T): List<String>
    where T : CharSequence,     // list, threshold 의 T타입은 모두 CharSequence, Comparable<T>타입을 만족해야한다.
          T : Comparable<T> {
    return list.filter { it > threshold }.map { it.toString() }
}
```

### non-null 제네릭 타입 : <T: Any>

T는 null도 포함하고 있기 때문에 non-null 타입으로 쓰고 싶을 경우 ` <T: Any>` 로 사용해야한다.

### 실행시 제네릭의 동작방식

> 실행시점에 제네릭 클래스의 인스턴스는 **타입정보가 없다!** -> JVM의 제네릭은 타입소거 (type erasure)를 사용하기 때문

- 타입소거의 장점
    - 저장해야하는 타입 정보의 크기가 줄어들어서 메모리 사용량이 감소한다.
- 타입소거의 한계
    - 실행시점에 타입인자를 검사할 수 없다. `if(xxx is List<String>)` -> compile error!
    - 이를 해결하기위해선 `*` (star projection) 을 사용한다. (`if( value is List<*>)`)
        - **`* (star projection)`: 인자를 알 수 없는 제네릭타입을 표현할 때 쓰는 것 (?)**
        - 타입파라미터가 2개이상일 경우 모두 * 를 붙여야한다. (ex. Map)

```kotlin
fun main() {
    val names = listOf(1, 2, 3)
    // 컴파일에러 발생한다. -> Kotlin: Cannot check for instance of erased type: List<String>
    if (names is List<String>) {
        println("names is List<String>")
    }

    // 해소법
    // but 컴파일러가 unchecked cast 워닝을 띄워줌 (동작은 잘된다)
    if (names is List<*>) {
        println("names is List<*>")
    }

    // 코틀린 컴파일러는 컴파일 시점에 타입 정보가 주어진 경우에는 is 검사를 수행하게 허용
    fun printSum(c: Collection<Int>) { // Int라는 타입정보가 주어졌기 때문에
        if (c is List<Int>) {  // is 검사가 가능함
            println(c.sum())
        }
    }
}
```

**이를 우회하는 방법: `inline & reified`**

- 함수를 inline으로 만들면 **타입인자가 지워지지 않게 할 수 있다**. == reify(실체화)
    - 그 이유는 **inline 함수는 컴파일 시점에 해당 로직이 바이트 코드로 들어가기 때문에** 실행시점에 타입이 지워지지 않는다.

```kotlin
// AS-IS
fun <T> isA(value: Any) = value is T // Error: Cannot check for instance of erased type: T

// TO-BE
inline fun <reified T> isA2(value: Any) = value is T
fun main() {
    println(isA2<String>("abc"))
}
```

```kotlin
public inline fun <reified R> kotlin.collections.Iterable<*>.filterIsInstance()

// 예제
listOf("Heejoo", 1, 0.5).filterIsInstance<String>() // -> listOf("Heejoo")

```

- **reified R == R타입이 실행시점에도 남아있게 실체화한다.**
    - **즉 refied(실체화)를 하기 위해선 실행시점에도 정보가 남아야 하기 때문에 inline 을 꼭 같이 써줘야 한다!**
    - reified는 타입정보를 남긴다는 의미이고, inline 은 코드를 바이트코드로 붙인다는 의미이다.

### reified 의 제약

> **inline 없이는 사용이 불가능하다.**

### 변셩 (variance) - 타입안정성을 보장하는 API 만들 수 있음

Any 타입에 String 타입을 넣어도 안전 But!
`List<Any>` 타입에 `List<String>` 타입은 안전X
그 이유는 A타입 리스트에 B타입 원소를 넣거나 수정할 경우 타입불일치가 발생하기 때문이다. (But 읽기전용은 어떻게 넘겨도 안전하다.)

### 무공변

`MutableList<String> 타입`, `MutableList<Any>타입` 가 있을 때, `MutableList<String>`은 `MutableList<Any>`의 하위 타입이 아니다. = 무공변

- `MutableList<String>` 타입의 값을 `MutableList<Any>` 타입을 갖는 파라미터의 함수로 전달이 가능한가? = **NO**
    - `MutableList<String>`이 `MutableList<Any>`의 하위 타입이 될 수 있는가? 로 해석가능하다.
- 타입인자들 사이에 하위타입이 성립되지 않을 경우 그 제네릭타입을 무공변이라고 한다.

### 공변

`List<String> 타입`, `List<Any> 타입`이 있을 때, `List<String>`는 `List<Any>` 로 대체 가능하다. = 공변

- `List<String>` 타입의 값을 `List<Any>` 타입을 갖는 파라미터의 함수로 전달이 가능한가? = **YES**
    - 그 이유는 읽기전용으로 원소 수정/추가 가 이루어지지 않으므로 원소들 내 타입불일치가 일어나도 전혀 영향이 없다.
- 타입인자들 사이에 하위타입이 성립 될 경우 그 제네릭타입을 공변이라고 한다.

**공변성 (out): 하위 타입 관계를 유지하는 성질**

- A가 B의 하위타입일 때 `클래스이름<A>`가 `클래스이름<B>`의 하위타입이면 `클래스이름`은 공변적이다. = 하위 타입 관계가 유지된다.
- 코틍린에서 제네릭 클래스가 타입 파라미터에 대해 공변적임을 표시하려면 타입 파라미터 앞에 **out**을 넣어야 한다.

```kotlin
interface AAA<out T> {
    fun aaa(): T
}
```

- 타입 파라미터를 공변적으로 지정하면 클래스 내부에서 그 파라미터를 사용하는 방법을 제한한다.
    - 클래스가 T타입의 값을 생산할 수(return)는 있지만 T타입의 값을 소비할(function argument) 수는 없다.
        - out 키워드를 붙이면 클래스 안에서 T를 사용하는 메소드가 아웃 위치에서만 T를 사용하게 허용하고 인 위치에서는 T를 사용하지 못하게 막는다.

```kotlin
interface AAA<out T> {
    // 첫번째 T: in, 두번째 T: out
//  fun aaa(t: T):T // compile error -> Type parameter T is declared as 'out' but occurs in 'in' position in type T
    fun aaa(t: Any): T // 정상적인 케이스
}
```

- out 키워드는 T의 사용법을 제한하며 T로인해 생기는 하위 타입 관계의 타입 안전성을 보장한다.
    - 왜냐하면 `t: T`가 가능했을 경우 t를 변형시키기 때문에 타입 불일치로 에러가 날 가능성이 있다. out은 이를 보장하기 때문이다. (소비가 불가능)
- out 규칙은 **외부에서 클래스를 잘못사용하는 일이 없기를 위해** 만들어 진 것이므로 내부구현에는 적용되지 않는다.

**반공변성 (in): in에서만 쓰인다**

- 타입 B가 타입 A의 하위 타입일 때, `클래스이름<A>`가  `클래스이름<B>`의 하위타입일 경우 `클래스이름<T>`는 T 에대해 반공변이다.
    - 예시: Producer (공변), Consumer (반공변)
- **`in`** 이 있으면 이 클래스의 메소드 안 인자로 정의되어 메소드에 의해 소비된다는 뜻이다.
    - in을 쓰면 T는 in위치에서만 쓸 수 있다.

```kotlin
// public fun <T> Iterable<T>.sortedWith(comparator: Comparator<in T>): List<T>

fun main() {
    val anyComparator = Comparator<Any> { e1, e2 ->
        e1.hashCode() - e2.hashCode()
    }

// sortedWith는 Comparator<String> 타입을 갖지만 Comparator<Any>도 가질 수 있었던 이유
// Comparator<Any> 안 로직은 String도 충분히 형변환에러가 안나기 때문에 안전하다 
    listOf("heejoo", "kim").sortedWith(anyComparator)
}
```

### 출처

- Kotlin in Action (9장)