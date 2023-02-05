### 코루틴 동작방식

> 코루틴 빌더에서 의해 코루틴이 만들어지고 만들어진 블록 안에서 코루틴이 실행된다.

- 코루틴은 가볍다. (경량쓰레드)

### 코루틴 빌더

> 코루틴을 만들어서 로직을 실행시키는 도구이다.

- 내부적으로 코루틴을 만들어서 반환한다.
- 빌더를 사용하기 위해서는 코루틴 Scope가 필요하다.
    - **즉 코루틴은 코루틴 스코프가 있어야 실행이 가능하다.**
- ex) `launch`, `runBlocking`

```kotlin
// launch 함수
public fun CoroutineScope.launch(   //  launch는 독립적으로 실행된게 아니다. scope의 함수이기 때문에 scope가 꼭 있어야 한다.
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    val newContext = newCoroutineContext(context)
    val coroutine = if (start.isLazy)
        LazyStandaloneCoroutine(newContext, block) else
        StandaloneCoroutine(newContext, active = true)
    coroutine.start(start, coroutine, block)
    return coroutine
}
```

**coroutineScope**

- CoroutineScope
- GlobalScope (프로그램의 라이프타임 전체범위)

### 일시 중단함수

- suspend
- delay
- join

### structured concurrency

- job들을 관리하지 않아도 (ex. join) 여러코루틴들을 기다려준다.



