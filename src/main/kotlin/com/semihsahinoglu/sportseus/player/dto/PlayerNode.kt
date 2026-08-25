package com.semihsahinoglu.sportseus.player.dto

data class PlayerNode(
    val id: Long? = null,
    val name: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val age: Int? = null,
    val birth: BirthNode? = null,
    val nationality: String? = null,
    val height: String? = null,
    val weight: String? = null,
    val number: Int? = null,        // sadece /profiles
    val position: String? = null,   // sadece /profiles
    val injured: Boolean? = null,   // sadece /players?id
    val photo: String? = null,
)