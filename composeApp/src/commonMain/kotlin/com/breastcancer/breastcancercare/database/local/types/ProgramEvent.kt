package com.breastcancer.breastcancercare.database.local.types

interface ProgramEvent {
    val id: Long
    val name: String
    val description: String
    val eventType: String
    val startTime: String?
    val endTime: String?
    val isOnline: Boolean

    val location:String?
}