package org.base.common.models.domain

data class Breed(
    val id: String,

    val name: String,
    val description: String,
    val image: String,

    val weight: String,
    val lifeSpan: String,

    val countryCode: String,
    val origin: String,
)
