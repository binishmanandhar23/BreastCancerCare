package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.models.UserDTO
import com.breastcancer.breastcancercare.repo.OnboardingRepository
import com.breastcancer.breastcancercare.screens.main.EditProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow


class SettingsViewModel(
    private val repo: OnboardingRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileUiState(loading = true))
    private val _saved = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val saved: SharedFlow<String> = _saved

    val state: StateFlow<EditProfileUiState> = _state.asStateFlow()

    private var currentId: Long? = null
    private var currentPassword: String = ""

    init {
        viewModelScope.launch {
            repo.getLoggedInUser().collect { dto ->
                if (dto == null) {
                    _state.update { it.copy(loading = false, error = "Not logged in") }
                } else {
                    currentId = dto.id
                    currentPassword = dto.password
                    _state.value = EditProfileUiState(
                        firstName = dto.firstName,
                        lastName = dto.lastName,
                        email = dto.email,
                        phoneNumber = dto.phoneNumber,
                        address = dto.address.orEmpty(),
                        loading = false,
                        canSave = canSave(
                            firstName = dto.firstName,
                            lastName = dto.lastName,
                            phone = dto.phoneNumber
                        )
                    )
                }
            }
        }
    }

    fun onFirstNameChange(value: String) = _state.update {
        val s = it.copy(firstName = value)
        s.copy(canSave = canSave(s.firstName, s.lastName, s.phoneNumber))
    }

    fun onLastNameChange(value: String) = _state.update {
        val s = it.copy(lastName = value)
        s.copy(canSave = canSave(s.firstName, s.lastName, s.phoneNumber))
    }

    fun onPhoneChange(value: String) = _state.update {
        val s = it.copy(phoneNumber = value)
        s.copy(canSave = canSave(s.firstName, s.lastName, s.phoneNumber))
    }

    fun save() {
        val id = currentId ?: return
        val s = _state.value
        if (!s.canSave) return

        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            val dto = UserDTO(
                id = id,
                firstName = s.firstName,
                lastName = s.lastName,
                email = s.email,
                phoneNumber = s.phoneNumber,
                address = s.address,
                password = currentPassword
            )
            try {
                repo.updateUser(dto)
                repo.setLoggedInUser(dto)
                _saved.tryEmit("Profile updated")
                _state.update { it.copy(loading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(loading = false, error = e.message ?: "Unknown error") }
            }
        }
    }

    private fun canSave(firstName: String, lastName: String, phone: String): Boolean {
        val digits = phone.filter(Char::isDigit)
        val phoneOk = digits.length in 8..15
        return firstName.isNotBlank() && lastName.isNotBlank() && phoneOk
    }

}
