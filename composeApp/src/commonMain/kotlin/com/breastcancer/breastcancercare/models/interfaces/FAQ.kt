package com.breastcancer.breastcancercare.models.interfaces

import com.breastcancer.breastcancercare.models.SuitabilityDTO

interface FAQ {
    val id: Long
    val question: String
    val answer: String
    val suitabilities: List<SuitabilityDTO>
}