package com.breastcancer.breastcancercare.models

import kotlinx.serialization.Serializable


@Serializable
data class FrequencySeries(val occurrence: Int, val dayOfWeek: String)