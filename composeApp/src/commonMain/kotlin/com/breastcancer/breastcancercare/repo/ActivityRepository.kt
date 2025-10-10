package com.breastcancer.breastcancercare.repo

import com.breastcancer.breastcancercare.database.local.dao.ActivityDAO
import com.breastcancer.breastcancercare.database.local.types.ActivityType
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.models.ActivityHistoryDTO
import com.breastcancer.breastcancercare.models.toActivityDTO
import com.breastcancer.breastcancercare.models.toDTO
import com.breastcancer.breastcancercare.models.toEntity
import com.breastcancer.breastcancercare.models.toSuitabilityDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class ActivityRepository(val activityDAO: ActivityDAO) {
    fun getAllActivities(userCategory: UserCategory) =
        activityDAO.getAllActivities(userCategory = userCategory.category).map { activityEntities ->
            activityEntities.map { it.toActivityDTO() }
        }

    fun getAllActivitiesByType(activityType: ActivityType) =
        activityDAO.getAllActivitiesByType(activityType = activityType.type)
            .map { activityEntities ->
                activityEntities.map { it.toActivityDTO() }
            }

    fun getEventsFromSelectedDate(date: LocalDate) =
        activityDAO.getEventsFromSelectedDate(date = date.toString())
            .map { eventEntities -> eventEntities.map { it.toActivityDTO() } }

    /*suspend fun getAllProgramsFromSelectedDate(date: LocalDate, selectedDatePrograms: (MutableList<ProgramDTO>) -> Unit) {
        getAllPrograms().collect { programs ->
            val selectedDatePrograms = mutableListOf<ProgramDTO>()
            programs.forEach { program ->
                if (checkIfDateHasProgram(
                        frequencyType = program.frequency,
                        selectedDate = date,
                        startDate = program.startDate,
                        endDate = program.endDate
                    )
                )
                    selectedDatePrograms.add(program)
            }
            selectedDatePrograms(selectedDatePrograms)
        }
    }*/

    fun getAllSuitabilities() = activityDAO.getAllSuitabilities().map { it.toSuitabilityDTO() }

    suspend fun getActivityById(id: Long) = activityDAO.getActivityById(id = id).toActivityDTO()


    suspend fun insertActivityHistory(activityHistoryDTO: ActivityHistoryDTO) =
        activityDAO.insertActivityHistoryEntity(activityHistoryEntity = activityHistoryDTO.toEntity())

    fun getActivityHistoryByActivityIdAndRegisteredDate(
        activityId: Long,
        userId: Long?,
        registeredDate: LocalDate?
    ): Flow<ActivityHistoryDTO?> =
        activityDAO.getActivityHistoryByActivityIdAndRegisteredDate(
            activityId = activityId,
            userId = userId,
            registeredDate = registeredDate.toString()
        ).map { it?.toDTO() }

    fun getActivityHistoryByRegisteredDate(
        userId: Long?,
        registeredDate: LocalDate?
    ): Flow<List<ActivityHistoryDTO>> =
        activityDAO.getActivityHistoryByRegisteredDate(
            userId = userId,
            registeredDate = registeredDate.toString()
        ).map { list -> list.map { it.toDTO() } }

    fun getAllActivityHistoryWithActivity(userId: Long?) = activityDAO.getAllActivityHistoryWithActivity(userId = userId)
        .map { activityHistoryWithActivityEntities -> activityHistoryWithActivityEntities.map { it.toDTO() } }

    fun insertActivity(){

    }
}