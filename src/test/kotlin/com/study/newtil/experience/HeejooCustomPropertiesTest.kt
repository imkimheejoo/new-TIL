package com.study.newtil.experience

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import io.kotest.matchers.shouldBe

@SpringBootTest
internal class HeejooCustomPropertiesTest{

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Test
    fun `properties가 잘 주입이 됐는지 확인`() {
        val heejooCustomProperties = applicationContext.getBean(HeejooCustomProperties::class.java)

        heejooCustomProperties.topic1 shouldBe "heejoo-topic1"
        heejooCustomProperties.camelCaseTopic shouldBe "heejoo-camelCaseTopic"
    }
}