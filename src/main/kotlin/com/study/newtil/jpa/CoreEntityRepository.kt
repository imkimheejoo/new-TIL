package com.study.newtil.jpa

interface CoreEntityRepository {
    fun findAllByAge(age: Int): List<CoreEntity>

    // TODO
    /**
     * fun save(entity: CoreEntity): CoreEntity 라고 하면 CoreEntityJpaRepository가 compile error 나는 이유
     */
    fun<S: CoreEntity> save(entity: S): S
    fun<S: CoreEntity> saveAll(entities: Iterable<S>): MutableList<S>
}