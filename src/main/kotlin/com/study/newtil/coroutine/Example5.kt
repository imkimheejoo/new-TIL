package com.study.newtil.coroutine

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

fun main5_ex1() {
    /**
    Unconfined: I'm working in thread:main
    Default: I'm working in thread:DefaultDispatcher-worker-1
    main runBlocking: I'm working in thread:main
    MyOwnThread: I'm working in thread:MyOwnThread
     */
    runBlocking {
        // runBlocking 의 context를 상속받아서 실행됨
        launch {
            println("main runBlocking: I'm working in thread:${Thread.currentThread().name}")
        }

        launch(Dispatchers.Unconfined) {
            println("Unconfined: I'm working in thread:${Thread.currentThread().name}")
        }

        // GlobalScope 에서 실행했을 때와 같은 쓰레드에서 실행됨
        launch(Dispatchers.Default) {
            println("Default: I'm working in thread:${Thread.currentThread().name}")
        }

//        launch(newSingleThreadContext("MyOwnThread")) {
//            println("MyOwnThread: I'm working in thread:${Thread.currentThread().name}")
//        }

        // 이 방법으로 해야 resuorce가 close되어서 메모리 누수를 방지
        newSingleThreadContext("MyOwnThread").use {
            launch(it) {
                println("MyOwnThread: I'm working in thread:${Thread.currentThread().name}")
            }
        }
    }
}

// jumping between threads
// withContext를 사용하면 코루틴이 쓰레드를 점프해서 다른 쓰레드에서 실행시킬 수 있다.
fun main5_ex2() {
    newSingleThreadContext("Ctx1").use {ctx1 ->
        newSingleThreadContext("Ctx2").use { ctx2 ->
            runBlocking(ctx1) {
                println("Thread: ${Thread.currentThread().name} Started in ctx1")
                withContext(ctx2) {
                    println("Thread: ${Thread.currentThread().name} Started in ctx2")
                }
                println("Thread: ${Thread.currentThread().name} Back to ctx1")
            }
        }
    }
}

// Job in the context
fun main5_ex3() {
    /**
    My Job is "coroutine#1":BlockingCoroutine{Active}@4abdb505
    My Job is "coroutine#2":StandaloneCoroutine{Active}@64f6106c
    My Job is "coroutine#3":DeferredCoroutine{Active}@7a30d1e6
     */
    runBlocking {
        println("My Job is ${coroutineContext[Job]}")   // coroutine context에서 job 설정을 꺼내서 출력

        launch {
            println("My Job is ${coroutineContext[Job]}")   // coroutine context에서 job 설정을 꺼내서 출력
        }

        async {
            println("My Job is ${coroutineContext[Job]}")   // coroutine context에서 job 설정을 꺼내서 출력
        }
    }
}

