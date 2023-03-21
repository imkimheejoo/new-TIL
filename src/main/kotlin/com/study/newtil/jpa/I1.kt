package com.study.newtil.jpa

// CoreEntityRepository (JpaRepository 상속X)
interface I1 {
    fun aaa()
}

// JpaRepository
interface I2 {
    fun aaa()
}

// CoreEntityJpaRepository
interface I3: I1,I2

// 같은 네이밍에 오버로딩

// 상속관계일 때 오버로딩은 어떻게 돌아가는가

// 정적디스패처

// 구현은닉

// 타입은닉
    // 업캐스팅, 다운캐스팅


// SimpleJpaRepository는 I2이자, I3
// I3는 I1이자, I2 (I1/I2 - 부모, I3 - 자식)
class Simple: I2,I3 {
    override fun aaa() {
        println("simple")
    }
}

fun main() {
    val simpleJpa: I3 = Simple()
    aaa(simpleJpa)
}

// I1이 I3의 부모이므로 I3타입도 들어올 수 있음
private fun aaa(i1: I1) {
    i1.aaa()
}

