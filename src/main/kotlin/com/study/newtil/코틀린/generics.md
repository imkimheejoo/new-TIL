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

### 출처

- Kotlin in Action (9장)