package com.study.newtil.coroutine

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main_ex1() {
    /**
     * doSomethingUsefulOne, doSomethingUsefulTwo 모두 같은 runBlockgin과 같은 코루틴스코프에서 실행되기 때문에
     * 비동기를 순서대로 실행된다.
     */
    runBlocking {
        val time = measureTimeMillis {
            val one = doSomethingUsefulOne() // 헤비한 메서드1
            val two = doSomethingUsefulTwo() // 헤비한 메서드2
            println("The answer is ${one + two}")
        }
        println("Completed in $time ms")    // 3036ms
    }
}

private suspend fun doSomethingUsefulOne(): Int {
    println("doSomethingUsefulOne, ${Thread.currentThread()}")
    delay(1000L)
    println("doSomethingUsefulOne Done!")
    return 13
}

private suspend fun doSomethingUsefulTwo(): Int {
    println("doSomethingUsefulTwo, ${Thread.currentThread()}")
    delay(2000L)
    println("doSomethingUsefulTwo Done!")
    return 29
}

fun main_ex2() {
    /**
     * 결과
    doSomethingUsefulOne, Thread[main,5,main]
    doSomethingUsefulTwo, Thread[main,5,main]
    doSomethingUsefulOne Done!
    doSomethingUsefulTwo Done!
    The answer is 42
    Completed in 2028 ms

    비동기를 동시에 실행한다.
     */
    runBlocking {
        val time = measureTimeMillis {
            val one = async { doSomethingUsefulOne() } // 헤비한 메서드1
            val two = async { doSomethingUsefulTwo() } // 헤비한 메서드2

            // await: job이 resume되기 전까지 기다린다.
            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms")    // 2028ms
    }

    /**
     * Deferred: Job을 상속한 interface
     */
}

fun main_ex3() {
    /**
    결과
    doSomethingUsefulOne, Thread[main,5,main]
    doSomethingUsefulTwo, Thread[main,5,main]
    Exception
    Delay done!
     */
    runBlocking {
        try {
            val time = measureTimeMillis {
                println("The answer is ${concurrentSum()}")
            }
            println("Completed in $time ms")
        } catch (e: Exception) {
        }

        runBlocking {
            delay(10000)    // 얘는 취소가 전파되지 않은 이유가 try-catch로 잡았기 때문에
            println("Delay done!")
        }
    }

}

private suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }

    delay(10)
    println("Exception")    // 예외가 발생하면 해당 scope에 있는 job들은 모두 cancel 된다.
    throw Exception()

    one.await() + two.await()
}

fun main_ex4() {
    /**
    결과
    one start, Thread[main,5,main]
    two will throw exception, Thread[main,5,main]
    Exception is java.lang.Exception
     */
    runBlocking {
        try {
            failedConcurrentSum()
        } catch (e: Exception) {
            println("Exception is $e")
        }
    }
}

private suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async {
        println("one start, ${Thread.currentThread()}")
        delay(3000L)
        println("one Done!")
        10
    }

    val two = async {
        println("two will throw exception, ${Thread.currentThread()}")
        throw Exception()   // 에러가 one에게도 전파되에 one의 job이 cancel됨
        20
    }

    one.await() + two.await()
}
