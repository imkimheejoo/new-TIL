package com.study.newtil.jpa

import java.util.Random

class FakeCoreEntityRepository(
    private val repository: MutableMap<Long, CoreEntity> = mutableMapOf()
): CoreEntityRepository {
    override fun findAllByAge(age: Int): List<CoreEntity> {
        return repository.values.filter { it.age == age }
    }

//    override fun <S : CoreEntity> save(entity: S): S {
    override fun save(entity: CoreEntity): CoreEntity {
        if(entity.id != 0L) {
            repository[entity.id] = entity
            return repository[entity.id]!!
        }
        val newId = Random().nextLong()
        repository[newId] = entity
        return repository[newId]!!
    }

    override fun <S : CoreEntity> saveAll(entities: Iterable<S>): MutableList<S> {
        TODO("Not yet implemented")
    }
}