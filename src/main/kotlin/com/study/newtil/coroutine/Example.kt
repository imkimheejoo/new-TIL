package com.study.newtil.coroutine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

fun main() {
    // global scope에서 launch를 한것
    GlobalScope.launch {
        delay(1000L)    // 일시 중단 함수
        println("World")
    }

    // 위에랑 같은 구현방식
    thread {
        Thread.sleep(1000L) // blocking 함수
        println("World")
    }

    // runBlocking도 코루틴 빌더
    runBlocking {// blockingCoroutin을 만들어서 반환
        // 명시적으로 blocking해야함
        delay(1000L) // delay는 coroutine scope이나 일시중단 함수에서만 사용이 가능하다.
    }

//    launch는 자신을 호출한 쓰레드를 blocking하지 않음
//    runBlocking은 호출한 쓰레드를 blocking함

    println("Hello")
    Thread.sleep(2000L)
}

// 관용적인 코드로 만들기
fun main2() {
    runBlocking {
        // 이 로직은 위 runBlocking과 아무 상관 없음
        GlobalScope.launch {
            delay(1000L)
            println("World")
        }

        println("Hello")
        delay(2000L)    // delay가 없으면 프로그램이 바로 끝나서 World가 안찍힘
    }
}

// main2의 마지막 delay를 안하고 다 출력할 수 있게 하는 방법
fun main3() = runBlocking {
    val job: Job = GlobalScope.launch {
        delay(1000L)
        println("World!")
    }

    println("Hello")
    job.join()  // job이 완료될때까지 기다림
}

// structured concurrency - scope들 끼리 관계를 맺어주는것
fun main4() = runBlocking {
    // 이렇게 하면 런치가 끝날때까지 기다려줌 = 상위코루틴이 하위코루틴을 다 기다려줌 (위 runBlocking의 coroutune scope을 이어서 쓴거기 때문에)
    this.launch {
        delay(1000L)
        println("World!")
    }

    println("Hello")
}

fun main5() = runBlocking {
    launch {
        myWorld()
    }

    println("Hello")
}

// suspend가 있어야 delay를 쓸 수 있음 (일시중단함수로 만들어줘야함)
private suspend fun myWorld() {
    delay(1000L)
    println("World")
}

// 프로세스가 끝나면 코루틴이 아직 동작하고 있어도 끝난다.
fun main6() = runBlocking {
// 하지만 scope를 runBlocking scope와 동일하게 하면 코루틴 끝날때까지 기다려줌
    GlobalScope.launch {
        repeat(1000) { i ->
            println("$i")
            delay(500L)
        }
    }
    delay(1400L)
}



