package com.study.newtil.experience.properties_config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExampleConfiguration {
    // as-is
    @Value("\${heejoo.study.properties.topic1}")
    private lateinit var topic1: String

    @Value("\${heejoo.study.properties.topic2}")
    private lateinit var topic2: String

    @Value("\${heejoo.study.properties.camelCaseTopic}")
    private lateinit var camelCaseTopic: String

    /**
     * properties를 한곳에 모아둠으로써 여러개를 쓰지 않아도 된다.
     * properties를 다른 configuration에서도 써야 한다면 as-is코드를 다른 configuration에 또 써야한다. 이를 개선할 수 있다.
     */
    // to-be
    private lateinit var customProperties: HeejooCustomProperties

    @Bean
    fun example(): String {
        return customProperties.topic1
    }
}