package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.models.UserDTO
import com.breastcancer.breastcancercare.repo.OnboardingRepository
import com.breastcancer.breastcancercare.states.LoginUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.combine

class OnboardingViewModel(val onboardingRepository: OnboardingRepository) : ViewModel() {

    private var _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private var _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    private var _agree = MutableStateFlow(false)
    val agree = _agree.asStateFlow()

    private var _userDTO = MutableStateFlow(UserDTO())
    val userDTO = _userDTO.asStateFlow()

    private var _emailValid = MutableStateFlow(true)
    val emailValid = _emailValid.asStateFlow()

    val phoneValid = userDTO
        .map { dto ->
            val digits = dto.phoneNumber.filter(Char::isDigit)
            digits.length in 8..15
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val emailValidInstant = userDTO
        .map { dto -> dto.email.isBlank() || (dto.email.contains("@") && dto.email.contains(".")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)
    val passwordValidInstant = combine(password, confirmPassword) { pw, cpw ->
        pw.length >= 6 && pw == cpw
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
    val canRegister = combine(userDTO, emailValidInstant, phoneValid, passwordValidInstant, agree) { dto, emailOK, phoneOK, pwOK, agreeOK ->
        dto.firstName.isNotBlank() &&
                dto.lastName.isNotBlank() &&
                emailOK && phoneOK && pwOK && agreeOK
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    private var _passwordValid = MutableStateFlow(true)
    val passwordValid = _passwordValid.asStateFlow()

    private var _loginUIState = MutableStateFlow<LoginUIState>(LoginUIState.Initial)
    val loginUIState = _loginUIState.asStateFlow()

    init {
        viewModelScope.launch {
            _loginUIState.update { LoginUIState.Loading }
            if (onboardingRepository.isLoggedIn())
                onboardingRepository.getLoggedInUser().collect { user ->
                    _loginUIState.update {
                        if (user != null) LoginUIState.Success("Welcome Back! ${user.firstName}")
                        else LoginUIState.Initial
                    }
                }
            else
                _loginUIState.update { LoginUIState.Initial }

        }
    }

    fun updatePassword(password: String) =
        _password.update { password }.also { updatePasswordValid(true) }

    fun updateConfirmPassword(password: String) =
        _confirmPassword.update { password }.also { updatePasswordValid(true) }

    fun toggleAgree(checked: Boolean) = _agree.update { checked }

    fun updateEmailValid(valid: Boolean) = _emailValid.update { valid }
    fun updatePasswordValid(valid: Boolean) = _passwordValid.update { valid }

    fun updateUserDTO(userDTO: UserDTO) =
        _userDTO.update {
            userDTO
        }.also {
            updateEmailValid(true)
        }

    fun onLogin() {
        viewModelScope.launch {
            checkEmailValidity().let { valid ->
                updateEmailValid(valid)
                if (valid)
                    onboardingRepository.getUser(userDTO.value.email).collect { user ->
                        if (user == null)
                            _loginUIState.update { LoginUIState.Error("User not found") }
                        else {
                            if (user.password == password.value) {
                                onboardingRepository.setLoggedInUser(user)
                                _loginUIState.update { LoginUIState.Success("Welcome Back! ${user.firstName}") }
                                reset()
                            } else
                                _loginUIState.update { LoginUIState.Error("Incorrect Password") }
                        }
                    }
            }
        }
    }

    fun onRegister() {
        if (!canRegister.value) {
            _loginUIState.update { LoginUIState.Error("Please check your inputs.") }
            return
        }
        viewModelScope.launch {
            try {
                val first = userDTO.value.firstName
                val toSave = userDTO.value.copy(password = password.value)
                onboardingRepository.insertUser(toSave)
                _loginUIState.update { LoginUIState.RegistrationSuccessful("Welcome! $first") }
                reset()
            } catch (e: Exception) {
                _loginUIState.update { LoginUIState.Error(e.message ?: "Unknown Error") }
            }
        }
    }

    fun onLogOut() = viewModelScope.launch {
        onboardingRepository.logOut()
        _loginUIState.update { LoginUIState.LoggedOut }
    }

    fun checkEmailValidity(): Boolean =
        userDTO.value.email.let { email -> email.contains("@") && email.contains(".") }

    fun reset() {
        _userDTO.update { UserDTO() }
        _password.update { "" }
        _confirmPassword.update { "" }
        _agree.update { false }
    }

}