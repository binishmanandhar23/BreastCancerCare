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
import com.breastcancer.breastcancercare.models.GuideDTO
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn


class FAQViewModel(private val faqRepository: FAQRepository) : ViewModel() {
    private var _faqUIState = MutableStateFlow<FAQUIState<List<FAQDTO>>?>(null)
    private val _suitabilities = MutableStateFlow<List<SuitabilityDTO>>(emptyList())
    val suitabilities = _suitabilities.asStateFlow()

    val faqUIState = _faqUIState.asStateFlow()
    // --- UI inputs (from screen) ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedSuitabilityKey = MutableStateFlow<String?>(null)
    val selectedSuitabilityKey = _selectedSuitabilityKey.asStateFlow()

    // --- Guides (demo data for now) ---
    private val _guides = MutableStateFlow(sampleGuides())

    // --- Derived flows (output to UI) ---
    private val allFaqs: StateFlow<List<FAQDTO>> =
        _faqUIState.map { (it as? FAQUIState.Success)?.data ?: emptyList() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val displayedFaqs: StateFlow<List<FAQDTO>> =
        combine(allFaqs, _selectedSuitabilityKey, _searchQuery) { base, key, q ->
            val bySuit = if (key.isNullOrEmpty()) base
            else base.filter { faq -> faq.suitabilities.any { it.key == key } }

            if (q.isBlank()) bySuit
            else {
                val needle = q.trim().lowercase()
                bySuit.filter {
                    it.question.lowercase().contains(needle) || it.answer.lowercase().contains(needle)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val displayedGuides: StateFlow<List<GuideDTO>> =
        combine(_guides, _searchQuery) { guides, q ->
            if (q.isBlank()) guides
            else {
                val needle = q.trim().lowercase()
                guides.filter {
                    it.title.lowercase().contains(needle) || it.summary.lowercase().contains(needle)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // --- Input updaters ---
    fun onSearchChange(q: String) { _searchQuery.value = q }
    fun onSuitabilityChange(key: String?) { _selectedSuitabilityKey.value = key }

    // --- Demo guides (multiplatform-safe: use label string for date) ---
    private fun sampleGuides(): List<GuideDTO> = listOf(
        GuideDTO(
            id = "g1",
            title = "Understanding Your Diagnosis",
            summary = "Key tests, staging basics, and what to ask at your first appointment.",
            category = "Diagnosis",
            readTimeMin = 4,
            updatedAtLabel = "Aug 2025"
        ),
        GuideDTO(
            id = "g2",
            title = "Managing Side Effects During Treatment",
            summary = "Practical tips for fatigue, nausea, and skin care during chemo or radiotherapy.",
            category = "Treatment",
            readTimeMin = 5,
            updatedAtLabel = "Jun 2025"
        ),
        GuideDTO(
            id = "g3",
            title = "Staying Well After Treatment",
            summary = "Follow-up schedules, lifestyle changes, and mental wellbeing after active treatment.",
            category = "Survivorship",
            readTimeMin = 3,
            updatedAtLabel = "Apr 2025"
        )
    )

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