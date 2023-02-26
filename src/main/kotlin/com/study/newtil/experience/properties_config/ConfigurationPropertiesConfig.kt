package com.study.newtil.experience.properties_config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(HeejooCustomProperties::class)
class ConfigurationPropertiesConfig