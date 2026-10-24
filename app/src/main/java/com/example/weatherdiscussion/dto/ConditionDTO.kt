package com.example.weatherdiscussion.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConditionDTO (
    val description: String,
    val icon: String
) {
}