package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.database.local.entity.ActivityEntity
import com.breastcancer.breastcancercare.database.local.entity.BlogEntity
import com.breastcancer.breastcancercare.database.local.entity.CategoryEntity
import com.breastcancer.breastcancercare.database.local.entity.SuitabilityEntity
import com.breastcancer.breastcancercare.database.local.types.CategoryType
import com.breastcancer.breastcancercare.database.local.types.FrequencyType
import com.breastcancer.breastcancercare.database.local.types.StartingStrongActivityType
import com.breastcancer.breastcancercare.database.local.types.Suitability
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.models.FAQDTO
import com.breastcancer.breastcancercare.models.toSuitabilityDTO
import com.breastcancer.breastcancercare.repo.BlogRepository
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
    val faqRepository: FAQRepository,
    val blogRepository: BlogRepository
) : ViewModel() {
    private var _splashUIState = MutableStateFlow<SplashUIState>(SplashUIState.Initial)
    val splashUIState = _splashUIState.asStateFlow()

    init {
        populateData()
        insertBlogData()
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
            calendarRepository.calendarDAO.insertAllActivities(
                listOf(
                    ActivityEntity(
                        id = 1,
                        title = "Early Breast Cancer Group – Mandurah",
                        description = "This group is for all women undergoing or recently completed treatment for early breast cancer.",
                        category = UserCategory.StartingStrong.category,
                        activityType = StartingStrongActivityType.Companion.StartingStrongActivityTypeEnum.SupportGroups.type,
                        startDate = LocalDate(2025, 8, 19).toString(),
                        endDate = null,
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Mandurah, Western Australia, Australia",
                        onlineLink = null,
                        audience = "People with breast cancer (by group type) ",
                        frequency = FrequencyType.OnceOff.type
                    ),
                    ActivityEntity(
                        id = 2,
                        title = "Young Women’s Early Breast Cancer Group – Online",
                        description = "This support group is for women with early breast cancer that are under 45 or with school aged children.",
                        activityType = EventType.Event.type,
                        startDate = LocalDate(2025, 8, 19).toString(),
                        endDate = null,
                        startTime = null,
                        endTime = null,
                        isOnline = true,
                        location = null, // ⬅️ online

                    ),
                    ActivityEntity(
                        id = 3,
                        title = "Online Partners of Women with Metastatic Breast Cancer Support Group",
                        description = "This group is for partners of women living with metastatic breast cancer.",
                        activityType = EventType.Event.type,
                        startDate = LocalDate(2025, 8, 21).toString(),
                        endDate = null,
                        startTime = LocalTime(19, 0, 0).toString(),
                        endTime = LocalTime(20, 0, 0).toString(),
                        isOnline = false,
                        location = "Cottesloe, Western Australia, Australia",
                        isFeatured = true,
                    ),
                    ActivityEntity(
                        id = 4,
                        title = "Young Women’s Early Breast Cancer Group – Hamersley",
                        description = "This support group is for women with early breast cancer that are under 45 or with school aged children.",
                        activityType = EventType.Event.type,
                        startDate = LocalDate(2025, 8, 22).toString(),
                        endDate = null,
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Hamersley, Western Australia, Australia",

                    ),
                    ActivityEntity(
                        id = 5,
                        title = "Young Women’s Metastatic Support Group – Online",
                        description = "This support group is for women living with metastatic breast cancer who are under 45 or have school-aged children.",
                        activityType = EventType.Event.type,
                        startDate = LocalDate(2025, 8, 26).toString(),
                        endDate = null,
                        startTime = null,
                        endTime = null,
                        isOnline = true,
                        location = null, // ⬅️ online

                    ),
                    ActivityEntity(
                        id = 6,
                        title = "Early Breast Cancer Group – Midland",
                        description = "This group is for all women undergoing or recently completed treatment for early breast cancer.",
                        activityType = EventType.Event.type,
                        startDate = LocalDate(2025, 8, 28).toString(),
                        endDate = null,
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        location = "Midland, Western Australia, Australia",

                    ),
                    ActivityEntity(
                        id = 7,
                        title = "Early Breast Cancer Group – Online",
                        description = "This group is for all women undergoing or recently completed treatment for early breast cancer.",
                        activityType = EventType.Event.type,
                        startDate = LocalDate(2025, 10, 7).toString(),
                        endDate = null,
                        startTime = LocalTime(12, 30, 0).toString(),
                        endTime = LocalTime(13, 30, 0).toString(),
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2022/08/bccwa-231025-natasha-242-WEB-ONLY.jpg",
                        isOnline = true,
                        location = null, // ⬅️ online

                    ),
                    ActivityEntity(
                        id = 8,
                        title = "Metastatic Support Group – Cottesloe",
                        description = "This support group is for women living with metastatic breast cancer.",
                        activityType = EventType.Event.type,
                        startDate = LocalDate(2025, 10, 1).toString(),
                        endDate = null,
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2022/08/21035_1222-2048x1365.jpg",
                        location = "Cottesloe, Western Australia, Australia",

                    ),
                    ActivityEntity(
                        id = 9,
                        title = "Metastatic Support Group – Mandurah",
                        description = "This support group is for women living with metastatic breast cancer.",
                        activityType = EventType.Event.type,
                        startDate = LocalDate(2025, 9, 10).toString(),
                        endDate = null,
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2022/08/21035_1154.jpg",
                        location = "Mandurah, Western Australia, Australia",

                    ),
                    ActivityEntity(
                        id = 10,
                        title = "Metastatic Support Group – Online",
                        description = "This support group is for women living with metastatic breast cancer.",
                        activityType = EventType.Event.type,
                        startDate = LocalDate(2025, 9, 10).toString(),
                        endDate = null,
                        startTime = null,
                        endTime = null,
                        isOnline = true,
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2022/08/bccwa-231025-natasha-239-PRINT-ONLY.jpg",
                        location = null, // ⬅️ online

                    ),
                    ActivityEntity(
                        id = 11,
                        title = "Young Women’s Early Breast Cancer Group – Murdoch",
                        description = "This support group is for women with early breast cancer that are under 45 or with school aged children.",
                        activityType = EventType.Event.type,
                        startDate = LocalDate(2025, 9, 11).toString(),
                        endDate = null,
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2022/08/bcwa-230614-stock-250-WEB-ONLY.jpg",
                        location = "Murdoch, Western Australia, Australia",

                    ),
                    ActivityEntity(
                        id = 12,
                        title = "Metastatic Support Group – Bunbury",
                        description = "This support group is for women living with metastatic breast cancer.",
                        activityType = EventType.Event.type,
                        startDate = LocalDate(2025, 9, 11).toString(),
                        endDate = null,
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2022/08/21035_0517.jpg",
                        location = "Bunbury, Western Australia, Australia",

                    ),
                    ActivityEntity(
                        id = 13,
                        title = "Living Well Discussion Group – Online",
                        description = "Discussion group for women who have recently completed treatment for early breast cancer; topics include fear of recurrence, side effects, stress management, healthy lifestyle and goal setting.",
                        activityType = EventType.Event.type,
                        startDate = LocalDate(2025, 9, 11).toString(),
                        endDate = null,
                        startTime = LocalTime(19, 0, 0).toString(),
                        endTime = LocalTime(20, 0, 0).toString(),
                        isOnline = true,
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2024/03/IMG20211208091308.jpg",
                        location = null, // ⬅️ online
                        isFeatured = true,
                    ),
                    ActivityEntity(
                        id = 14,
                        title = "Early Breast Cancer Group – Hamersley",
                        description = "This group is for all women undergoing or recently completed treatment for early breast cancer.",
                        activityType = EventType.Event.type,
                        startDate = LocalDate(2025, 9, 12).toString(),
                        endDate = null,
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2022/08/bcwa-230620-stock-32-WEB-ONLY.jpg",
                        location = "Hamersley, Western Australia, Australia",

                    ),
                    ActivityEntity(
                        id = 15,
                        title = "Metastatic Support Group – Joondalup",
                        description = "This support group is for women living with metastatic breast cancer.",
                        activityType = EventType.Event.type,
                        startDate = LocalDate(2025, 9, 19).toString(),
                        endDate = null,
                        startTime = null,
                        endTime = null,
                        isOnline = false,
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2022/08/21035_0433.jpg",
                        location = "Joondalup, Western Australia, Australia",

                    ),
                    ActivityEntity(
                        id = 16,
                        title = "Living Well Early Breast Cancer Support Group – Bunbury",
                        description = "Group for women living well after treatment; Thursdays 7–8pm covering fear of recurrence, side effects, stress management, healthy lifestyle and goal setting.",
                        activityType = EventType.Event.type,
                        startDate = LocalDate(2025, 11, 6).toString(),
                        endDate = null,
                        startTime = LocalTime(19, 0, 0).toString(),
                        endTime = LocalTime(20, 0, 0).toString(),
                        isOnline = false,
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2024/01/bcwa-230620-stock-36-WEB-ONLY.jpg",
                        location = "Bunbury, Western Australia, Australia",
                    ),
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

    private fun insertBlogData() {
        //Categories
        viewModelScope.launch(Dispatchers.IO) {
            blogRepository.insertAllCategories(
                categories = listOf(
                    CategoryEntity(CategoryType.BreastCancer.type, "Breast Cancer"),
                    CategoryEntity(CategoryType.SupportServices.type, "Support Services"),
                    CategoryEntity(CategoryType.InTheNews.type, "In the News"),
                    CategoryEntity(CategoryType.Volunteering.type, "Volunteering"),
                    CategoryEntity(CategoryType.OurClients.type, "Our Clients")
                )
            )
        }
        //Blogs
        viewModelScope.launch(Dispatchers.IO) {
            blogRepository.insertAllBlogs(
                blogs = listOf(
                    // 1) Melanie Rowley – Support for Metastatic Breast Cancer
                    BlogEntity(
                        slug = "melanie-rowley-support-for-metastatic-breast-cancer",
                        title = "Melanie Rowley: Support for Metastatic Breast Cancer",
                        body = "Metastatic breast care nurses play a critical role in supporting individuals living with life-limiting cancer, offering benefits to both the individual and the healthcare system. At Breast Cancer Care WA, our dedicated Metastatic Breast Care Nurse, Melanie Rowley, funded by the McGrath Foundation, provides essential care and support to more than 120 clients across Western Australia each year.\n" +
                                "Living with metastatic breast cancer (MBC) is often unpredictable. Clients may experience fluctuating phases of stability and instability, requiring frequent transitions between hospitals, treatment centres, or hospice care. Despite the established discipline of breast care nursing in oncology, access to specialised breast care nurses for people with MBC remains limited in Australia. This is where Melanie’s role becomes vital.\n" +
                                "\n" +
                                "“One of my clients described living with MBC as being on a never-ending rollercoaster,” Melanie shares.\n" +
                                "“My role is to ensure that my clients and their loved ones never have to face any of this alone. I am there as their key contact to provide ongoing emotional support, as well as acting as their advocate to ensure that all of their unique needs are met.”\n" +
                                "\n" +
                                "Melanie brings a wealth of experience to her role, having worked in oncology nursing since 2004. Her background includes extensive experience as a clinical nurse, clinical nurse specialist, staff development, and palliative care. She is a passionate advocate for person-centred care, believing strongly in the importance of focusing on clients’ values, beliefs, and needs to help them live as well as possible.\n" +
                                "\n" +
                                "“By identifying my clients’ holistic needs and addressing them early, I hope to ease some of the worries and anxieties that clients experience as a result of their disease,” Melanie explains.\n" +
                                "“This gives clients the space they need to focus on what really matters to them, so that they can go on to live their very best life possible.”\n" +
                                "\n" +
                                "Estimates suggest that between 9,000 and 12,000 Australians are currently living with MBC. Whilst people are living longer with this disease due to the advances in treatment, the impacts of the disease remain profound. Approximately 1 in 15 breast cancer diagnoses in Australia are metastatic or stage IV at the time of the first diagnosis, and 25-30% of people initially diagnosed with early breast cancer will eventually develop metastatic breast cancer.\n" +
                                "\n" +
                                "The support provided by metastatic breast care nurses like Melanie is invaluable. They not only offer clinical guidance but also provide emotional support, advocacy, and personalised care to help clients navigate the complexities of living with metastatic breast cancer. Through her work, Melanie ensures that those living with MBC have a dedicated advocate and a caring supporter throughout their experience.",
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2024/09/bcwa-230614-stock-401-WEB-ONLY-652x437.jpg",
                        categories = listOf(
                            CategoryEntity(CategoryType.BreastCancer.type, "Breast Cancer"),
                            CategoryEntity(CategoryType.SupportServices.type, "Support Services")
                        ),
                        tags = listOf("Metastatic", "support services")
                    ),

                    // 2) Meet Michelle – 2024 IGA Purple Bra Day Ambassador
                    BlogEntity(
                        slug = "2024-iga-purple-bra-day-ambassador",
                        title = "Meet Michelle – Our 2024 IGA Purple Bra Day Ambassador",
                        body = "Michelle shares her breast cancer journey and why she’s championing IGA Purple Bra Day to help ensure free nursing, counselling, and practical support remain available across WA. She reflects on diagnosis, treatment, recovery, and the power of community-led fundraising to support those facing breast cancer.\n\n" +
                                "Her message encourages early screening, open conversations, and getting behind Purple Bra Day so families don’t face breast cancer alone.",
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2024/07/img-3841-jpg-afdb8d-652x437.jpg",
                        categories = listOf(
                            CategoryEntity(CategoryType.InTheNews.type, "In the News"),
                            CategoryEntity(CategoryType.OurClients.type, "Our Clients")
                        ),
                        tags = listOf("Purple Bra Day", "Ambassador", "Fundraising")
                    ),

                    // 3) Basil’s Ride
                    BlogEntity(
                        slug = "basils-ride-pedaling-through-challenges-with-hope",
                        title = "Basil’s Ride: Pedaling Through Challenges with Hope",
                        body = "Every May, Basil rides 200 km to support people facing breast cancer, while living with multiple sclerosis himself. His story highlights resilience, staying active, and inspiring others to give. Funds raised help deliver nursing, counselling, and practical support across WA, including regional communities.\n\n" +
                                "Basil’s message: focus on what you can do, share hope, and back services that make a direct difference.",
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2024/05/Basils-Ride-2-652x437.jpg",
                        categories = listOf(
                            CategoryEntity(CategoryType.OurClients.type, "Our Clients"),
                            CategoryEntity(CategoryType.InTheNews.type, "In the News")
                        ),
                        tags = listOf("Fundraising", "Community stories")
                    ),

                    // 4) Closing the Divide – World Cancer Day
                    BlogEntity(
                        slug = "world-cancer-day",
                        title = "Closing the Divide: A Call to Action on World Cancer Day",
                        body = "On World Cancer Day, BCCWA calls for fair and equitable care—no matter where people live or their cultural background. Around one in five clients live in regional/remote WA, so BCCWA delivers online nursing, counselling, and groups to reduce access barriers, and is developing a Reconciliation Action Plan to improve outcomes for First Nations people.\n\n" +
                                "Together we can ‘close the care gap’ so every person affected by breast cancer receives timely, quality support.",
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2024/02/World-Cancer-Day-eDM-header-600-x-400-px.png",
                        categories = listOf(
                            CategoryEntity(CategoryType.InTheNews.type, "In the News"),
                            CategoryEntity(CategoryType.SupportServices.type, "Support Services")
                        ),
                        tags = listOf("World Cancer Day", "Equity", "First Nations")
                    ),

                    // 5) A day of wellness and rejuvenation
                    BlogEntity(
                        slug = "a-day-of-wellness-and-rejuvenation",
                        title = "A day of wellness and rejuvenation",
                        body = "Hosted by metastatic breast care nurse Melanie Rowley and project coordinator Chandrika Gibson, BCCWA’s Metastatic Wellness Day offered restorative activities—chair yoga, sound healing, massage, clay therapy, and a shared lunch. The day created calm, connection, and practical relief for clients navigating the demands of metastatic disease.\n\n" +
                                "BCCWA will continue offering programs that nurture wellbeing alongside clinical care.",
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2023/10/Massage2-652x437.jpg",
                        categories = listOf(
                            CategoryEntity(CategoryType.SupportServices.type, "Support Services"),
                            CategoryEntity(CategoryType.OurClients.type, "Our Clients")
                        ),
                        tags = listOf("Wellbeing", "Living well", "Metastatic")
                    ),

                    // 6) Metastatic Breast Cancer Awareness Day 2023
                    BlogEntity(
                        slug = "metastatic-breast-cancer-awareness-day-2023",
                        title = "Metastatic Breast Cancer Awareness Day 2023",
                        body = "October 13 recognises people living with metastatic breast cancer and the often ‘invisible’ realities of the disease. Jill’s poem “I Look Well” captures the fear, fatigue, side-effects, and financial strain many experience, even when they appear outwardly fine. BCCWA provides dedicated support groups, a specialist metastatic nurse, and tailored education to help people live as well as possible.",
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2022/08/21035_0547-652x437.jpg",
                        categories = listOf(
                            CategoryEntity(CategoryType.BreastCancer.type, "Breast Cancer"),
                            CategoryEntity(CategoryType.InTheNews.type, "In the News")
                        ),
                        tags = listOf("Metastatic", "Awareness")
                    ),

                    // 7) IGA Purple Bra Day – Mix 94.5 Interview
                    BlogEntity(
                        slug = "mix-94-5-interview",
                        title = "IGA Purple Bra Day Interview with Mix 94.5’s Pete and Kymba",
                        body = "CEO Max Clarke and Ambassador Kate joined Mix 94.5 to talk all things Purple Bra Day—why the campaign matters, how funds are used, and how to get involved. Listeners can tune in via the embedded audio and learn how to support free BCCWA services across Western Australia.",
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2023/09/MicrosoftTeams-image-32-e1693979081970-652x437.jpg",
                        categories = listOf(
                            CategoryEntity(CategoryType.InTheNews.type, "In the News")
                        ),
                        tags = listOf("Purple Bra Day", "Media", "Fundraising")
                    ),

                    // 8) Hancock Prospecting donation
                    BlogEntity(
                        slug = "hancock-prospecting",
                        title = "Donation from Hancock Prospecting to Fund Financial Assistance Program",
                        body = "BCCWA announced Hancock Prospecting funding for its Financial Assistance Program over the next two years, helping people facing breast cancer—especially in regional and remote WA—with essential costs during treatment and recovery. The partnership expands reach and impact where government funding does not apply.",
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2023/08/bcwa-230614-stock-190-WEB-ONLY-652x437.jpg",
                        categories = listOf(
                            CategoryEntity(CategoryType.InTheNews.type, "In the News"),
                            CategoryEntity(CategoryType.SupportServices.type, "Support Services")
                        ),
                        tags = listOf("Donation", "Financial assistance", "Partnerships")
                    ),

                    // 9) Survivorship spotlight – NDIS for lymphoedema
                    BlogEntity(
                        slug = "survivorship-spotlight-accessing-the-ndis-for-lymphoedema-support",
                        title = "Survivorship spotlight: accessing the NDIS for lymphoedema support",
                        body = "Eva’s story shows that gaining NDIS access for lymphoedema is possible—though persistence is often required. After an initial decline, her plan was approved, funding compression garments and allied health supports that restored day-to-day function. The article links to guidance and rebates, and notes Cancer Australia’s estimate that ~20% of cancer survivors experience secondary lymphoedema.\n\n" +
                                "Advice: understand the criteria, find clinicians who can document needs clearly, and keep appealing.",
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2023/08/iStock-936849736-652x437.jpg",
                        categories = listOf(
                            CategoryEntity(CategoryType.OurClients.type, "Our Clients"),
                            CategoryEntity(CategoryType.SupportServices.type, "Support Services")
                        ),
                        tags = listOf("NDIS", "Lymphoedema", "Survivorship")
                    ),

                    // 10) Meet Kate – 2023 IGA Purple Bra Day Ambassador
                    BlogEntity(
                        slug = "2023-iga-purple-bra-day-ambassador-kate",
                        title = "Meet Kate, our 2023 IGA Purple Bra Day Ambassador",
                        body = "Two years after surgery and treatment, Kate shares how she turned her experience into advocacy and fundraising for Purple Bra Day—raising over $13k during treatment and continuing to inspire others to screen early and support local services. She’s returned to work, manages ongoing side-effects, and still leans on BCCWA counselling and groups when needed.\n\n" +
                                "Her advice for would-be fundraisers: ‘just do it’—every effort helps families across WA.",
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2023/07/bcwa-230614-stock-9-WEB-ONLY-652x437.jpg",
                        categories = listOf(
                            CategoryEntity(CategoryType.OurClients.type, "Our Clients"),
                            CategoryEntity(CategoryType.InTheNews.type, "In the News")
                        ),
                        tags = listOf("Purple Bra Day", "Ambassador", "Survivorship")
                    ),

                    // 11) Addressing Disparities in Breast Cancer Care
                    BlogEntity(
                        slug = "addressing-disparities-in-breast-cancer-care-a-step-towards-equality",
                        title = "Addressing Disparities in Breast Cancer Care: A Step towards Equality",
                        body = "In honour of National Reconciliation Week, BCCWA highlights inequities experienced by Aboriginal people—lower screening rates, later diagnoses, and treatment delays—driven by cultural, geographic, and socioeconomic barriers. Referencing research such as Banham (2019), the piece outlines BCCWA actions (online supports, community partnerships, and a Reconciliation Action Plan) to close the gap.\n\n" +
                                "Equity in access, information, and culturally safe support is essential to improve outcomes.",
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2023/05/National-Reconciliation-Week-600-%C3%97-400px-1.png",
                        categories = listOf(
                            CategoryEntity(CategoryType.InTheNews.type, "In the News"),
                            CategoryEntity(CategoryType.SupportServices.type, "Support Services"),
                            CategoryEntity(CategoryType.BreastCancer.type, "Breast Cancer")
                        ),
                        tags = listOf("First Nations", "Equity", "Reconciliation")
                    ),

                    // 12) Meet our incredible volunteer, Cate!
                    BlogEntity(
                        slug = "meet-our-incredible-volunteer-cate",
                        title = "Meet our incredible volunteer, Cate!",
                        body = "Cate was diagnosed with breast cancer at 39 and later returned as a dedicated BCCWA volunteer—supporting Purple Bra Day, Corporate Golf Day, Wildcats Pink Game, and more. She describes counselling and support groups as ‘amazing,’ and shares memorable moments of community generosity while fundraising. If you’d like to help, BCCWA is always welcoming new volunteers.",
                        image = "https://www.breastcancer.org.au/wp-content/uploads/2023/05/CateStevens-Volunteer-652x437.jpg",
                        categories = listOf(
                            CategoryEntity(CategoryType.Volunteering.type, "Volunteering"),
                            CategoryEntity(CategoryType.OurClients.type, "Our Clients")
                        ),
                        tags = listOf("Volunteering", "Community")
                    )
                )
            )
        }
    }
}