package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.database.local.types.UserCategory
import com.breastcancer.breastcancercare.models.UserDTO
import com.breastcancer.breastcancercare.repo.OnboardingRepository
import com.breastcancer.breastcancercare.states.LoginUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
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

    private var _phoneValid = MutableStateFlow(true)
    val phoneValid = _phoneValid.asStateFlow()

    private var _canRegister = MutableStateFlow(false)
    val canRegister = _canRegister.asStateFlow()

    private var _passwordValid = MutableStateFlow(true)
    val passwordValid = _passwordValid.asStateFlow()

    private var _loginUIState = MutableStateFlow<LoginUIState>(LoginUIState.Initial)
    val loginUIState = _loginUIState.asStateFlow()


    fun updatePassword(password: String) =
        _password.update { password }

    fun updateConfirmPassword(password: String) =
        _confirmPassword.update { password }

    fun toggleAgree(checked: Boolean) = _agree.update { checked }

    fun updateUserDTO(userDTO: UserDTO) =
        _userDTO.update {
            userDTO
        }

    init {
        canRegister()
    }

    fun canRegister() {
        viewModelScope.launch {
            userDTO.collectLatest { dto ->
                val emailOK =
                    dto.email.isBlank() || (dto.email.contains("@") && dto.email.contains("."))
                val phoneOK = dto.phoneNumber.filter(Char::isDigit).length in 8..15
                _emailValid.update { _ -> emailOK }
                _phoneValid.update { _ -> phoneOK }
            }
        }
        viewModelScope.launch {
            combine(password, confirmPassword) { pw, cpw ->
                pw.length >= 6 && pw == cpw
            }.collectLatest {
                _passwordValid.update { _ -> it }
            }
        }
        viewModelScope.launch {
            combine(
                userDTO,
                passwordValid,
                agree
            ) { dto, pwOK, agreeOK ->
                val emailOK =
                    dto.email.isBlank() || (dto.email.contains("@") && dto.email.contains("."))
                val phoneOK = dto.phoneNumber.filter(Char::isDigit).length in 8..15
                dto.firstName.isNotBlank() &&
                        dto.lastName.isNotBlank() &&
                        emailOK && phoneOK && pwOK && agreeOK
            }.collectLatest {
                _canRegister.update { _ -> it }
            }
        }
    }

    suspend fun onLogin() {
        _loginUIState.update { LoginUIState.Loading }
        onboardingRepository.getUser(userDTO.value.email).let { user ->
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

    fun onRegister() {
        if (!this.canRegister.value)
            _loginUIState.update { LoginUIState.Error("Please check your inputs.") }
        else
            viewModelScope.launch {
                try {
                    val toSave = userDTO.value.copy(
                        password = password.value,
                        userCategory = UserCategory.Undefined
                    )
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