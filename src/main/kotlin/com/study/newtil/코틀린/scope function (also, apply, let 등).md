## Scope Function

### scope function 의 정의

> 객체 안에 범위를 만들어서 코드를 실행시킬 수 있게하는 (람다?)함수
>
> 예) also, apply, let, run, with
>

### scope function의 특징 - 가독성 Good

- scope function을 사용하면 임시로 코드가 실행되는 범위가 형성되며, 이 안에서 객체의 정보를 조금 더 간결하게 사용할 수 있다.
    - 가령, 객체의 특정 프로퍼티를 객체이름 없이 엑세스가 가능하다.
    - 즉 코드가 간결해서 읽기 쉽다. 그러므로 객체관련해서 어떤 코드가 실행되어야 할 때 유용하다.
  ```kotlin
  data class Person(val name: String)

  fun main() {
    val person = Person("heejoo")
    person.run { 
        println(name)
    }

  }
  ```

### scope function을 선택하는 기준

> The choice mainly depends on your intent and the consistency of use in your project.

- 어떤의도로 만들어진 것인지, 어떤 일관성(?)을 갖는지에 따라 scope function을 결정하면 된다.

![img.png](function_selection.png) 출처. kotlin docs

### let

```kotlin
public inline fun <T, R> T.let(block: (T) -> R): R  // it으로 객체 접근, 람다 결과값을 return
```

- 객체의 확장함수로, 람다의 반환값이 return 된다.
- **null 이 아닌** 객체를 이용해서 **어떤 로직을 실행시킬 때** 사용하면 좋다. (Executing a lambda on non-null objects)

```kotlin
data class Person(val name: String, val age: Int, val country: String? = null)

fun main() {
    // 예제1
    val person = Person("heejoo", 28)
    val introduceComment = person.let {
        "안녕, 나는 ${it.age}세 ${it.name} 라고 해"
    }

    println(introduceComment)

    // 또는
    person.let {
        val comment = "안녕, 나는 ${it.age}세 ${it.name} 라고 해"
        println(comment)
    }

    // 예제2
    val country: String? = person.country
    val len = country?.let {          // country가 null이 아닐 때만 let이 실행된다.
        println("I'm from $country")
        it.length
    } ?: 0
}
```

### run

```kotlin
public inline fun <T, R> T.run(block: T.() -> R): R // this로 객체접근, 람다 결과값을 반환
public inline fun <R> run(block: () -> R): R    // 객체 없이도 사용가능하며, 람다 결과값을 반환

// 예제
data class Person(val name: String)

fun main() {
    val person = Person("heejoo")
    person.run {

    }

    kotlin.run { }
}

```

- run 은 **객체 생성(초기화)하고 그 객체에 관련된 로직을 실행시키려할** 때 유용하다.
    - 그래서 run 은 let 과 같은 성격을 지닌다.

```kotlin
val service = MultiportService("https://example.kotlinlang.org", 80)

val result = service.run {
    port = 8080
    query(prepareRequest() + " to port $port")
}

// the same code written with let() function:
val letResult = service.let {
    it.port = 8080
    it.query(it.prepareRequest() + " to port ${it.port}")
}
```

### with

```kotlin
public inline fun <T, R> with(receiver: T, block: T.() -> R): R // this로 접근(생략가능), 람다 결과값을 반환
```

- 객체에 여러 작업을 해야할 때 권장한다. (Grouping function calls on an object)
- 람다 결과값아 없는 (void) 로직을 구현할 때 권장한다. -> 왜..?
- 예제

```kotlin
val numbers = mutableListOf("one", "two", "three")
with(numbers) {
    println("'with' is called with argument $this")
    println("It contains $size elements")
}
```

- 어떤 값을 계산하고 그 값을 반환 할 때 with을 권장한다.
    - let 이랑 같은 성격을 띄고 있으나 let은 it을 써야하기 때문에 with가 가능한 상황이면 let보단 with가 더 간결하다.
        - 그렇다면 let은 언제? `null` check 를 간편하게 하고 싶을 때 사용한다. (Executing a lambda on non-null objects)

```kotlin
val numbers = mutableListOf("one", "two", "three")
val firstAndLast = with(numbers) {
    "The first element is ${first()}," +
        " the last element is ${last()}"
}
println(firstAndLast)
```

### apply

```kotlin
public inline fun <T> T.apply(block: T.() -> kotlin.Unit): T    // it으로 접근, 자기자신을 반환
```

- apply는 람다식에서 반환값이 없다. (Unit)
- apply는 수신객체의 정보들을 변경해서(no copy) 리턴할 때 주로 쓰인다. (비유하자면 객체 configuration)
    - 수신객체에 copy를 적용함으로써 새로운 객체를 만들 때는 let이 더 어울린다. (객체 내부정보롤 변경시킨게 아니기 때문)
        - 어차피 apply는 자기자신을 반환하고 람다 리턴타입도 void라 새 객체로 바뀌지 않음

```kotlin
data class Person(val name: String, val age: Int)

fun main() {
    val heejoo = Person("heejoo", 28).apply {
        it.copy(age = 29)   // 람다 반환값이 void이기 때문에 copy된 새 객체는 변수에 할당 되지 않는다.
    }

    assertTrue(heejoo.age == 28)
}
```

- 함수체이닝이 가능하기 때문에 객체에서 복잡한 세팅이 필요할 때 가독성을 좋게 짤 수 있다.

```kotlin
val adam = Person("Adam").apply {
    age = 32
    city = "London"
}
println(adam)

val newAdam = adam
    .apply {
        age = it.age + 1
    }.apply {
        city = "LA"
    }

println(newAdam)
```

### also

```kotlin
public inline fun <T> T.also(block: (T) -> kotlin.Unit): T  // it으로 접근, 자기자신 반환
```

- 객체를 통해 무언가 부가적인 작업(additional effects)을 해야할 때 권장한다. (ex. 로깅)
- apply와 같이 람다 결과반환 타입이 없다.

```kotlin
data class Person(val name: String, val age: Int) {
    companion object {
        fun init(name: String, age: Int): Person {
            Person(
                name = name.also { println("Hi. My name is $name") },   // 로깅(부가적인 작업)
                age = age,
            )
        }
    }
}

val person = Person("heejoo", 28)
```

### context object: this / it

- this: lambda receiver  (람다 수신자) 로 객체 정보 접근
- it: lambda argument (람다 인자) 로 객체 정보 접근

```kotlin
fun main() {
    val str = "String"
    str.run {
        println("$this length: $length")
    }

    str.let {
        println(it.length)
    }
}
```

**this**

- run, with, apply
- `this.변수명`, `this.함수명()` 이렇게 사용가능하고 심지어 this 생략가능하다.
- 하지만 this를 생략하면 외부 함수와 object context의 함수와 헷갈릴 가능성이 높다.
    - 따라서 컨텍스트 객체를 수신자(this)로 사용하는 것은 **주로 객체 멤버에서 작동하는 람다(해당 기능 호출 또는 속성 할당)에 권장**된다.

```kotlin
fun main() {
    val person = Person("heejoo")
    person.run {
        println(introduce() + name) // introduce는 Person 클래스에 정의된 함수가 아닌데 그렇게 해석 될 여지가 있다.
    }
}

fun introduce(): String = "hi"

data class Person(val name: String)
```

**it**

- let, also
- 이름을 딱히 짓지 않으면 디폴트 `it`으로 접근한다. (이름 지정가능)
- this 와 달리 생략이 불가능하다.
- 코드블록에서 여러 변수들이 사용될때 좋다... (왜?)

```kotlin
fun getRandomInt(): Int {
    return Random.nextInt(100).also { it ->
        writeToLog("getRandomInt() generated value $it")
    }
}
```

### Return value

- **context object를 반환: apply, also**
    - **함수체이닝이 가능하다.**

```kotlin
val numberList = mutableListOf<Double>()

numberList
    .also { println("Populating the list") }
    .apply {
        add(2.71)
        add(3.14)
        add(1.0)
    }
    .also { println("Sorting the list") }
    .sort()

```

- **lambda 식의 반환값을 반환: let, run, with**
    - 람다식의 결과를 변수에 넣고 결과에 관한 다음 작업을 할 때 유용하다.

```kotlin
val numbers = mutableListOf("one", "two", "three")
val countEndsWithE = numbers.run {
    add("four")
    add("five")
    count { it.endsWith("e") }
}
println("There are $countEndsWithE elements that end with e.")
```

- lambda result 값을 무시하고 void 형식으로도 사용이 가능하다.

```kotlin
val numbers = mutableListOf("one", "two", "three")
with(numbers) {
    val firstItem = first()
    val lastItem = last()
    println("First item: $firstItem, last item: $lastItem")
}
```

### 출처
- https://kotlinlang.org/docs/scope-functions.html
- http://batmask.net/index.php/2021/12/10/286/