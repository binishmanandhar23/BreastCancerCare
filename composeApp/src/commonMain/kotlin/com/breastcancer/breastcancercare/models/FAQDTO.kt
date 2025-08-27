package com.breastcancer.breastcancercare.models

import com.breastcancer.breastcancercare.database.local.entity.FAQEntity
import com.breastcancer.breastcancercare.models.interfaces.FAQ

data class FAQDTO(
    override val id: Long,
    override val question: String,
    override val answer: String,
    override val suitabilities: List<SuitabilityDTO>
) : FAQ

fun FAQDTO.toFAQEntity() = FAQEntity(
    id = id,
    question = question,
    answer = answer,
    suitabilities = suitabilities.map { it.toSuitabilityEntity() })

fun FAQEntity.toFAQDTO() = FAQDTO(
    id = id,
    question = question,
    answer = answer,
    suitabilities = suitabilities.map { it.toSuitabilityDTO() })
