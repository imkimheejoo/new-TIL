## Scope Function

### scope function 의 정의

> 객체 안에서 코드를 실행시킬 수 있게하는 (람다?)함수
>
> 예) also, apply, let, run, with
>

### scope function의 특징

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
public inline fun <T, R> T.let(block: (T) -> R): R
```
- 객체의 확장함수로, 람다의 반환값이 return 된다.
- null 이 아닌 객체를 이용해서 **어떤 로직을 실행시킬 때** 사용하면 좋다.


- 예제
```kotlin
data class Person(val name: String, val age: Int)

fun main() {
  val person = Person("heejoo", 28)
  val introduceComment = person.let {
    "안녕, 나는 ${it.age}세 ${it.name} 라고 해"
  }
  
  println(introduceComment)
}
```

### run (작성중)
```kotlin
public inline fun <T, R> T.run(block: T.() -> R): R
```
```kotlin
public inline fun <R> run(block: () -> R): R
```
```kotlin
data class Person(val name: String)

fun main() {
    val person = Person("heejoo")
    person.run { 
        
    }
  
  kotlin.run {  }
}
```