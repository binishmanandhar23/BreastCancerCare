package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.database.local.dao.FAQDAO
import com.breastcancer.breastcancercare.models.FAQDTO
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
    val faqUIState = _faqUIState.asStateFlow()

    init {
        populateData()
        getAllFAQs()
    }

    private fun populateData() {
        viewModelScope.launch(Dispatchers.IO) {
            faqRepository.insertAll(
                listOf(
                    FAQDTO(1, "Question 1", "Answer 1"),
                    FAQDTO(2, "Question 2", "Answer 2"),
                    FAQDTO(3, "Question 3", "Answer 3"),
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