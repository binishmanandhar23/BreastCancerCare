package com.breastcancer.breastcancercare.models

import com.breastcancer.breastcancercare.database.local.entity.FAQEntity
import com.breastcancer.breastcancercare.models.interfaces.FAQ

data class FAQDTO(
    override var id: Long,
    override var question: String,
    override var answer: String
): FAQ

fun FAQDTO.toFAQEntity() = FAQEntity(id = id, question = question, answer = answer)

fun FAQEntity.toFAQDTO() = FAQDTO(id = id, question = question, answer = answer)
