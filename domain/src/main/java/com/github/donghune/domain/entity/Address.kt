package com.github.donghune.domain.entity

data class Address(
    val id: Int = 0,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val groupId: Int
)