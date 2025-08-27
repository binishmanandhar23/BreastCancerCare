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
    val faqUIState = _faqUIState.asStateFlow()

    init {
        populateSuitabilities()
        populateData()
        getAllFAQs()
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

    private fun populateData() {
        viewModelScope.launch(Dispatchers.IO) {
            faqRepository.insertAll(
                listOf(
                    FAQDTO(
                        1,
                        question = "What do I do if I find a lump?",
                        answer = "In young women, breasts can feel much lumpier around the time of the menstrual cycle. If a lump persists after one menstrual cycle, contact your doctor. In older women, who have gone through the menopause, contact your doctor as soon as possible.\n" +
                                "\n" +
                                "All breasts have areas of lumpiness that fluctuate with the menstrual cycle, however if lumps are irregular, unchanging or slowly enlarging or new you should contact your doctor.",
                        suitabilities = listOf(
                            SuitabilityDTO(
                                key = Suitability.Early.key,
                                name = "Early Breast cancer, post active treatment"
                            )
                        )
                    ),
                    FAQDTO(
                        2,
                        question = "Are most breast lumps found to be cancerous?",
                        answer = "Nearly 80% of breast lumps are benign (not cancerous). Lumpy breasts are very common and they can change with different times of the menstrual cycle. Any lump that is new or unusual should be checked by a doctor.",
                        suitabilities = listOf(
                            SuitabilityDTO(
                                key = Suitability.Early.key,
                                name = "Early Breast cancer, post active treatment"
                            )
                        )
                    ),
                    FAQDTO(
                        3,
                        question = "When I’m checking my breasts, am I looking for lumps?",
                        answer = "A lump in the breast is only one change that may indicate breast cancer. All women regardless of age are encouraged to be breast aware. Click here to find out how.\n" +
                                "\n" +
                                "The changes you should look for include;\n" +
                                "\n" +
                                "A lump or lumpiness\n" +
                                "An area that feels different to the rest of the breast\n" +
                                "An area of thickening\n" +
                                "Changes to the skin such as dimpling, puckering or redness\n" +
                                "Nipple discharge or bleeding from the nipple\n" +
                                "Nipple itchiness, scaly skin or ulcers around the nipple\n" +
                                "New nipple inversion\n" +
                                "New and persistent pain\n" +
                                "If you notice any of the above changes please consult your GP or health professional as soon as possible.",
                        suitabilities = listOf(
                            SuitabilityDTO(
                                key = Suitability.Early.key,
                                name = "Early Breast cancer, post active treatment"
                            )
                        )
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