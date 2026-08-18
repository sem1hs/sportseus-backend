package com.semihsahinoglu.sportseus.league.dto

data class CountryNode(
    val name: String,
    val code: String?,         // bazı uluslararası liglerde null olabilir
    val flag: String?
)
