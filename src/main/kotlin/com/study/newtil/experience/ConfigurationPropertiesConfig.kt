package com.study.newtil.experience

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(HeejooCustomProperties::class)
class ConfigurationPropertiesConfig