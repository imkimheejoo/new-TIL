package com.study.newtil.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield

fun example1() = runBlocking {
    val job = launch(context = Dispatchers.Default) {
        repeat(1000) { i ->
            println("Job: I'm sleeping $i...")
            delay(500L)
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancel()
    job.join()
    println("main Now I can quit")
}

//suspend function 이 없으면 cancel이 의도와 다르게 동작함
fun example2() = runBlocking {
    val job = launch(context = Dispatchers.Default) {
        val startTime = System.currentTimeMillis()
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) {
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("Job: I'm sleeping ${i++}...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main Now I can quit")

    // 결과
    /*Job: I'm sleeping 0...
    Job: I'm sleeping 1...
    Job: I'm sleeping 2...
    main: I'm tired of waiting!
    Job: I'm sleeping 3...
    Job: I'm sleeping 4...
    main Now I can quit*/
}


// suspend function 써서 cancellation에 협조하기
fun example3() = runBlocking {
    val job = launch(context = Dispatchers.Default) {
        val startTime = System.currentTimeMillis()
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) {
            yield()
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("Job: I'm sleeping ${i++}...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main Now I can quit")

    // 결과
//    Job: I'm sleeping 0...
//    Job: I'm sleeping 1...
//    Job: I'm sleeping 2...
//    main: I'm tired of waiting!
//    main Now I can quit
}

// isActive 써서 cancellation에 협조하기
fun example4() = runBlocking {
    val job = launch(context = Dispatchers.Default) {
        val startTime = System.currentTimeMillis()
        var nextPrintTime = startTime
        var i = 0
        // isActive: 확장프로퍼티 - 코루틴이 취소됐는지 check해주는 프로퍼티
        println("isActive $isActive")
        while (isActive) {
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("Job: I'm sleeping ${i++}...")
                nextPrintTime += 500L
            }
        }
        println("isActive $isActive")
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main Now I can quit")

    // 결과
//    Job: I'm sleeping 0...
//    Job: I'm sleeping 1...
//    Job: I'm sleeping 2...
//    main: I'm tired of waiting!
//    main Now I can quit
}

// 일시중단 함수가 재개될 때 cancellation exception이 뜬다.
fun example5() = runBlocking {
    val job = launch(context = Dispatchers.Default) {
        try {
            val startTime = System.currentTimeMillis()
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) {
                if (System.currentTimeMillis() >= nextPrintTime) {
                    yield()
                    println("Job: I'm sleeping ${i++}...")
                    nextPrintTime += 500L
                }
            }
        } catch (e: Exception) {    // cancel된 코루틴이 재개할 때 JobCancellationException을 던진다.
            println("exception: [$e]")
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main Now I can quit")
}

// timeout
fun example6() = runBlocking {
    // withTimeoutOrNull을 쓰면 timeout일 때 null을 반환한다.
    withTimeout(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping... $i")
            delay(500L)
        }
    }

    // 결과
//    I'm sleeping... 0
//    I'm sleeping... 1
//    I'm sleeping... 2
//    Exception in thread "main" kotlinx.coroutines.TimeoutCancellationException: Timed out waiting for 1300 ms
}



