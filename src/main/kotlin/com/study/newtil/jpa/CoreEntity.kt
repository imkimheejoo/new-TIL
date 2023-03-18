package com.study.newtil.jpa

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
// TODO: 2023/03/18 왜 open class..?
open class CoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column
    var name: String,
    @Column
    var age: Int,
)