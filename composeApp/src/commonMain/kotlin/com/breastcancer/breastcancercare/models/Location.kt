package com.breastcancer.breastcancercare.models

import kotlinx.serialization.Serializable

@Serializable
data class Location(val suburb: String, val state: String, val country: String)
