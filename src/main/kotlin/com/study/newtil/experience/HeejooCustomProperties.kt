package com.study.newtil.experience

import javax.validation.constraints.NotEmpty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated

/**
 * ConfigurationProperties를 쓰기 위해선 bean에 등록해야한다. 따라서 configuration을 만들어 따로 등록해야한다. (@EnableConfigurationProperties)
 * 안하면 'Not registered via @EnableConfigurationProperties, marked as Spring component, or scanned via @ConfigurationPropertiesScan' 에러 뜸
 */
@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "heejoo.study.properties")
data class HeejooCustomProperties(
    @field:NotEmpty
    val topic1: String,
    @field:NotEmpty
    val topic2: String,
    @field:NotEmpty
    val camelCaseTopic: String,
)
