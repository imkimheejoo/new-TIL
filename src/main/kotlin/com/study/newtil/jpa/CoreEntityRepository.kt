package com.study.newtil.jpa


interface CoreEntityRepository {
    fun findAllByAge(age: Int): List<CoreEntity>

    // TODO: 2023/03/18 save는 충돌 왜 안나지?
    fun save(entity: CoreEntity): CoreEntity
//    fun<S: CoreEntity> save(entity: S): S -> 이렇게 구현하려면 CoreEntity는 open class가 되어야 한다. CoreEntity의 자식엔티티도 저장이 가능하기 때문에

    /**
     * fun saveAll(entities: Iterable<CoreEntity>): MutableList<CoreEntity> 라고 하면 CoreEntityJpaRepository가 compile error 나는 이유

     * Kotlin: Inherited platform declarations clash: The following declarations have the same JVM signature (saveAll(Ljava/lang/Iterable;)Ljava/util/List;):
        fun saveAll(entities: Iterable<CoreEntity>): MutableList<CoreEntity> defined in com.study.newtil.jpa.CoreEntityJpaRepository
        fun <S : CoreEntity?> saveAll(p0: MutableIterable<S>): MutableList<S> defined in com.study.newtil.jpa.CoreEntityJpaRepository
     */
//    fun saveAll(entities: Iterable<CoreEntity>): MutableList<CoreEntity>

    // TODO: 2023/03/18 이렇게 짜도 충돌나는거 아닌가?
    fun <S: CoreEntity> saveAll (entities: Iterable<S>): MutableList<S>

}