package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.models.UserDTO
import com.breastcancer.breastcancercare.repo.OnboardingRepository
import com.breastcancer.breastcancercare.states.LoginUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.combine
import com.breastcancer.breastcancercare.utils.text.removeSpaces


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

    val passwordLengthValid = password
        .map { it.length >= 6 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val passwordsMatch = combine(password, confirmPassword) { pw, cpw ->
        cpw.isNotBlank() && pw == cpw
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val baseValid = combine(
        userDTO,
        emailValidInstant,
        phoneValid,
        passwordLengthValid,
        passwordsMatch
    ) { dto, emailOK, phoneOK, lenOK, matchOK ->
        dto.firstName.isNotBlank() &&
                dto.lastName.isNotBlank() &&
                emailOK && phoneOK && lenOK && matchOK
    }

    val canRegister = baseValid
        .combine(agree) { base, agreeOK -> base && agreeOK }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)


    private var _loginUIState = MutableStateFlow<LoginUIState>(LoginUIState.Initial)
    val loginUIState = _loginUIState.asStateFlow()

    fun updatePassword(password: String) =
        _password.update { password }

    fun updateConfirmPassword(password: String) =
        _confirmPassword.update { password }

    fun toggleAgree(checked: Boolean) = _agree.update { checked }

    fun updateEmailValid(valid: Boolean) = _emailValid.update { valid }

    fun updateUserDTO(userDTO: UserDTO) =
        _userDTO.update {
            userDTO
        }.also {
            updateEmailValid(true)
        }
    fun updateEmail(email: String) {
        _userDTO.update { it.copy(email = email.removeSpaces()) }
    }

    fun updatePhone(phone: String) {
        _userDTO.update { it.copy(phoneNumber = phone.removeSpaces()) }
    }

    fun onLogin() {
        _loginUIState.update {
            LoginUIState.Loading
        }
        viewModelScope.launch(Dispatchers.Default) {
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
        if (!canRegister.value)
            _loginUIState.update { LoginUIState.Error("Please check your inputs.") }
        else
            viewModelScope.launch {
                try {
                    val toSave = userDTO.value.copy(password = password.value)
                    onboardingRepository.insertUser(toSave)
                    _loginUIState.update { LoginUIState.RegistrationSuccessful(successMessage = "Registration Successful!") }
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

    fun clearTransientLoginState() {
        _loginUIState.update { LoginUIState.Initial }
    }
}