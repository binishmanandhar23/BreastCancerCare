package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.database.local.entity.EventEntity
import com.breastcancer.breastcancercare.database.local.entity.ProgramEntity
import com.breastcancer.breastcancercare.database.local.entity.SuitabilityEntity
import com.breastcancer.breastcancercare.database.local.types.EventType
import com.breastcancer.breastcancercare.database.local.types.FrequencyType
import com.breastcancer.breastcancercare.database.local.types.Suitability
import com.breastcancer.breastcancercare.models.FAQDTO
import com.breastcancer.breastcancercare.models.toSuitabilityDTO
import com.breastcancer.breastcancercare.repo.CalendarRepository
import com.breastcancer.breastcancercare.repo.FAQRepository
import com.breastcancer.breastcancercare.repo.OnboardingRepository
import com.breastcancer.breastcancercare.states.SplashUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class SplashViewModel(
    val onboardingRepository: OnboardingRepository,
    val calendarRepository: CalendarRepository,
    val faqRepository: FAQRepository
) : ViewModel() {
    private var _splashUIState = MutableStateFlow<SplashUIState>(SplashUIState.Initial)
    val splashUIState = _splashUIState.asStateFlow()

    init {
        populateData()
        checkLoginStatus()
    }

    private fun populateData() {
        val allSuitabilities = listOf(
            SuitabilityEntity(
                key = Suitability.Early.key,
                name = "Early breast cancer, post active treatment",
                description = null
            ),
            SuitabilityEntity(
                key = Suitability.Metastatic.key,
                name = "Metastatic breast cancer ",
                description = null
            ),
            SuitabilityEntity(
                key = Suitability.Newly.key,
                name = "Newly diagnosed and in treatment",
                description = null
            )
        )
        viewModelScope.launch(Dispatchers.IO) {
            calendarRepository.calendarDAO.insertAllSuitabilities(allSuitabilities)
        }
        viewModelScope.launch {
            calendarRepository.calendarDAO.insertAllEvents(
                listOf(
                    EventEntity(
                        id = 1,
                        name = "Early Breast Cancer Group – Mandurah",
                        description = "This group is for all women undergoing or recently completed treatment for early breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 8, 19).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Mandurah, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 2,
                        name = "Young Women’s Early Breast Cancer Group – Online",
                        description = "This support group is for women with early breast cancer that are under 45 or with school aged children.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 8, 19).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = true,
                        location = null, // ⬅️ online
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 3,
                        name = "Online Partners of Women with Metastatic Breast Cancer Support Group",
                        description = "This group is for partners of women living with metastatic breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 8, 21).toString(),
                        startTime = LocalTime(19, 0, 0).toString(),
                        endTime = LocalTime(20, 0, 0).toString(),
                        isOnline = false,
                        location = "Cottesloe, Western Australia, Australia",
                        isFeatured = true,
                    ),
                    EventEntity(
                        id = 4,
                        name = "Young Women’s Early Breast Cancer Group – Hamersley",
                        description = "This support group is for women with early breast cancer that are under 45 or with school aged children.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 8, 22).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Hamersley, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 5,
                        name = "Young Women’s Metastatic Support Group – Online",
                        description = "This support group is for women living with metastatic breast cancer who are under 45 or have school-aged children.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 8, 26).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = true,
                        location = null, // ⬅️ online
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 6,
                        name = "Early Breast Cancer Group – Midland",
                        description = "This group is for all women undergoing or recently completed treatment for early breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 8, 28).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Midland, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 7,
                        name = "Early Breast Cancer Group – Online",
                        description = "This group is for all women undergoing or recently completed treatment for early breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 2).toString(),
                        startTime = LocalTime(12, 30, 0).toString(),
                        endTime = LocalTime(13, 30, 0).toString(),
                        isOnline = true,
                        location = null, // ⬅️ online
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 8,
                        name = "Metastatic Support Group – Cottesloe",
                        description = "This support group is for women living with metastatic breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 3).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Cottesloe, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 9,
                        name = "Metastatic Support Group – Mandurah",
                        description = "This support group is for women living with metastatic breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 10).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Mandurah, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 10,
                        name = "Metastatic Support Group – Online",
                        description = "This support group is for women living with metastatic breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 10).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = true,
                        location = null, // ⬅️ online
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 11,
                        name = "Young Women’s Early Breast Cancer Group – Murdoch",
                        description = "This support group is for women with early breast cancer that are under 45 or with school aged children.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 11).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Murdoch, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 12,
                        name = "Metastatic Support Group – Bunbury",
                        description = "This support group is for women living with metastatic breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 11).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Bunbury, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 13,
                        name = "Living Well Discussion Group – Online",
                        description = "Discussion group for women who have recently completed treatment for early breast cancer; topics include fear of recurrence, side effects, stress management, healthy lifestyle and goal setting.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 11).toString(),
                        startTime = LocalTime(19, 0, 0).toString(),
                        endTime = LocalTime(20, 0, 0).toString(),
                        isOnline = true,
                        location = null, // ⬅️ online
                        isFeatured = true,
                    ),
                    EventEntity(
                        id = 14,
                        name = "Early Breast Cancer Group – Hamersley",
                        description = "This group is for all women undergoing or recently completed treatment for early breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 12).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Hamersley, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 15,
                        name = "Metastatic Support Group – Joondalup",
                        description = "This support group is for women living with metastatic breast cancer.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 9, 19).toString(),
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Joondalup, Western Australia, Australia",
                        isFeatured = false,
                    ),
                    EventEntity(
                        id = 16,
                        name = "Living Well Early Breast Cancer Support Group – Bunbury",
                        description = "Group for women living well after treatment; Thursdays 7–8pm covering fear of recurrence, side effects, stress management, healthy lifestyle and goal setting.",
                        eventType = EventType.Event.type,
                        date = LocalDate(2025, 10, 2).toString(),
                        startTime = LocalTime(19, 0, 0).toString(),
                        endTime = LocalTime(20, 0, 0).toString(),
                        isOnline = false,
                        location = "Bunbury, Western Australia, Australia",
                        isFeatured = true,
                    ),
                )

            )
        }
        viewModelScope.launch {
            calendarRepository.calendarDAO.insertAllPrograms(
                events = listOf(
                    ProgramEntity(
                        id = 1,
                        name = "Yoga",
                        description = "Daily Yoga Session",
                        eventType = EventType.Program.type,
                        startTime = LocalTime(7, 0, 0).toString(),
                        endTime = LocalTime(8, 0, 0).toString(),
                        isOnline = true,
                        location = null,
                        startDate = LocalDate(2025, 8, 1).toString(),
                        endDate = LocalDate(2035, 8, 1).toString(),
                        suitabilities = allSuitabilities,
                        frequency = FrequencyType.Daily.type
                    )
                )
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            faqRepository.insertAll(
                listOf(
                    FAQDTO(
                        1,
                        question = "What do I do if I find a lump?",
                        answer = "In young women, breasts can feel much lumpier around the time of the menstrual cycle. If a lump persists after one menstrual cycle, contact your doctor. In older women, who have gone through the menopause, contact your doctor as soon as possible.\n" +
                                "\n" +
                                "All breasts have areas of lumpiness that fluctuate with the menstrual cycle, however if lumps are irregular, unchanging or slowly enlarging or new you should contact your doctor.",
                        suitabilities = allSuitabilities.find { it.key == Suitability.Early.key }
                            ?.toSuitabilityDTO()?.let {
                            listOf(
                                it
                            )
                        } ?: emptyList()
                    ),
                    FAQDTO(
                        2,
                        question = "Are most breast lumps found to be cancerous?",
                        answer = "Nearly 80% of breast lumps are benign (not cancerous). Lumpy breasts are very common and they can change with different times of the menstrual cycle. Any lump that is new or unusual should be checked by a doctor.",
                        suitabilities = allSuitabilities.find { it.key == Suitability.Early.key }
                            ?.toSuitabilityDTO()?.let {
                            listOf(
                                it
                            )
                        } ?: emptyList()
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
                        suitabilities = allSuitabilities.find { it.key == Suitability.Early.key }
                            ?.toSuitabilityDTO()?.let {
                            listOf(
                                it
                            )
                        } ?: emptyList()
                    ),
                )
            )
        }
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            delay(500L)
            if (onboardingRepository.isLoggedIn())
                onboardingRepository.getLoggedInUser().collect { user ->
                    _splashUIState.update {
                        if (user != null)
                            SplashUIState.LoggedIn
                        else
                            SplashUIState.NotLoggedIn
                    }
                }
            else
                _splashUIState.update {
                    SplashUIState.NotLoggedIn
                }

        }
    }
}