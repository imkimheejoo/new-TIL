package com.study.newtil.coroutine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


fun mainn() {
    GlobalScope.launch {
        val suspendFetchUserData = suspendFetchUserData()
        val suspendCacheUserData = suspendCacheUserData(suspendFetchUserData)
    }

    // 위 코루틴의 실행동작을 코드로 나타낸것
    myCoroutine(MyContinuation())
}

suspend fun suspendFetchUserData() = "user_name"
suspend fun suspendCacheUserData(user: String) = user

fun myCoroutine(cont: MyContinuation) {
    when (cont.label) {
        0 -> {
            println("\nmyCoroutine(), label: ${cont.label}")
            cont.label = 1
            fetchUserData(cont)
        }
        1 -> {
            println("\nmyCoroutine(), label: ${cont.label}")
            val userData = cont.result
            cont.label = 2
            cacheUserData(userData, cont)
        }
    }
}

fun cacheUserData(user: String, cont: MyContinuation) {
    println("cacheUserData()")
    val result = cont.result
    println("작업완료 - cached user name: $result")
    cont.resumeWith(Result.success(result))
}

fun fetchUserData(cont: MyContinuation) {
    println("fetchUserData()")
    val result = "input_user_name"
    println("작업완료 - user name: $result")
    cont.resumeWith(Result.success(result))
}


class MyContinuation(
    override val context: CoroutineContext = EmptyCoroutineContext
) : Continuation<String> {

    var label = 0   // 다음에 재개될 데이터
    var result = ""

    override fun resumeWith(result: Result<String>) {
        this.result = result.getOrThrow()
        println("Continuation.resumeWith()")
        myCoroutine(this)
    }
}