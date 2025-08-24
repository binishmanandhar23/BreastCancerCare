package com.breastcancer.breastcancercare.models.interfaces

interface UserInterface {
    val id: Long
    val firstName: String
    val lastName: String
    val password: String
    val email: String
    val phoneNumber: String
    val address: String?
}