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
    private var _passwordValid = MutableStateFlow(true)
    val passwordValid = _passwordValid.asStateFlow()

    private var _loginUIState = MutableStateFlow<LoginUIState>(LoginUIState.Initial)
    val loginUIState = _loginUIState.asStateFlow()

    init {
        viewModelScope.launch {
            _loginUIState.update { LoginUIState.Loading }
            if (onboardingRepository.isLoggedIn())
                onboardingRepository.getLoggedInUser().collect { userDTO ->
                    if (userDTO != null)
                        _loginUIState.update {
                            LoginUIState.Success("Welcome Back! ${userDTO.firstName}")
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
                            if (user.password == userDTO.value.password) {
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
        val first = userDTO.value.firstName
        val last = userDTO.value.lastName
        val pw = password.value
        val cpw = confirmPassword.value
        updatePasswordValid(pw == cpw && pw.isNotEmpty() && pw.length > 6)
        updateEmailValid(checkEmailValidity())
        val canSubmit = first.isNotBlank() && last.isNotBlank() &&
                emailValid.value && passwordValid.value && agree.value
        if (canSubmit)
            viewModelScope.launch {
                try {
                    onboardingRepository.insertUser(userDTO.value)
                    _loginUIState.update { LoginUIState.Success("Welcome! $first") }
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

    fun reset() = _userDTO.update { UserDTO() }.also { _password.update { "" } }

}