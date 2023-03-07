package com.study.newtil.jpa

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class JpaTestController(
    private val coreEntityRepository: CoreEntityRepository,
) {

    @PostMapping("/test")
    fun test(): Any {
        return coreEntityRepository.save(
            CoreEntity(name = "heejoo")
        )
    }
}