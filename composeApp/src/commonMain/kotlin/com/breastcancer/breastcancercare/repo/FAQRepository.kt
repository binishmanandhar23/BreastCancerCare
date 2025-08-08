package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.FAQDAO
import com.breastcancer.breastcancercare.models.FAQDTO
import com.breastcancer.breastcancercare.models.toFAQDTO
import com.breastcancer.breastcancercare.models.toFAQEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FAQRepository(val faqdao: FAQDAO) {
    suspend fun insertAll(faqs: List<FAQDTO>){
        faqs.map { fAQDTO -> fAQDTO.toFAQEntity() }.let {
            faqdao.insertAll(it)
        }
    }

    fun getAllFAQs(): Flow<List<FAQDTO>> =
        faqdao.getAllFAQs().map { faqEntity ->
            faqEntity.map { it.toFAQDTO() }
        }

}