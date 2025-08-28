package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.database.local.dao.FAQDAO
import com.breastcancer.breastcancercare.database.local.types.Suitability
import com.breastcancer.breastcancercare.models.FAQDTO
import com.breastcancer.breastcancercare.models.SuitabilityDTO
import com.breastcancer.breastcancercare.repo.FAQRepository
import com.breastcancer.breastcancercare.states.FAQUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FAQViewModel(private val faqRepository: FAQRepository) : ViewModel() {
    private var _faqUIState = MutableStateFlow<FAQUIState<List<FAQDTO>>?>(null)
    private val _suitabilities = MutableStateFlow<List<SuitabilityDTO>>(emptyList())
    val suitabilities = _suitabilities.asStateFlow()

    val faqUIState = _faqUIState.asStateFlow()

    init {
        populateSuitabilities()
        getAllFAQs()
        viewModelScope.launch {
            faqRepository.getAllSuitabilities().collect { list ->
                _suitabilities.value = list
            }
        }
    }

    private fun populateSuitabilities() {
        viewModelScope.launch(Dispatchers.IO) {
            faqRepository.insertAllSuitabilities(
                listOf(
                    SuitabilityDTO(
                        key = Suitability.Early.key,
                        name = "Early Breast cancer, post active treatment"
                    ),
                    SuitabilityDTO(
                        key = Suitability.Metastatic.key,
                        name = "Metastatic Breast Cancer"
                    ),
                    SuitabilityDTO(
                        key = Suitability.Newly.key,
                        name = "Newly Diagnosed and in Treatment"
                    ),
                )
            )
        }
    }


    private fun getAllFAQs() = viewModelScope.launch {
        _faqUIState.update { FAQUIState.Loading() }
        delay(2000L)
        faqRepository.getAllFAQs().collect { fAQDTOS ->
            _faqUIState.update {
                if (fAQDTOS.isEmpty())
                    FAQUIState.Error("Empty List")
                else
                    FAQUIState.Success(fAQDTOS)
            }
        }
    }
}