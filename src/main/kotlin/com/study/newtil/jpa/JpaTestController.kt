package com.study.newtil.jpa

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class JpaTestController(
    private val coreEntityRepository: CoreEntityRepository,
) {

    @PostMapping("/test/{id}")
    fun test(@PathVariable id:Long): Any {
        return if(id == 0L) {
            coreEntityRepository.save(
                CoreEntity(name = "heejoo-new", age = 28)
            )
        } else {
            return coreEntityRepository.save(
                CoreEntity(id = id, name = "heejoo", age = 28)
            )
        }
    }
}