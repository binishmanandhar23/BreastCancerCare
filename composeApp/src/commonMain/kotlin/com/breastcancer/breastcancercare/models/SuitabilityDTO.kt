package com.breastcancer.breastcancercare.models

import com.breastcancer.breastcancercare.database.local.entity.SuitabilityEntity

data class SuitabilityDTO(val key: String, val name: String, val description: String? = null)


fun SuitabilityEntity.toSuitabilityDTO() =
    SuitabilityDTO(key = key, name = name, description = description)

fun SuitabilityDTO.toSuitabilityEntity() =
    SuitabilityEntity(key = key, name = name, description = description)