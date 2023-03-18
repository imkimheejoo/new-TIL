package com.study.newtil.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface CoreEntityJpaRepository: CoreEntityRepository, JpaRepository<CoreEntity, Long>