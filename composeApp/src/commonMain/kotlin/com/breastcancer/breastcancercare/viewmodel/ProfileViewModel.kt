package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breastcancer.breastcancercare.database.local.dao.UserDao
import com.breastcancer.breastcancercare.screens.main.ProfileUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userDao: UserDao
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState(loading = true))
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userDao.getLoggedInUser()
                .map { loggedIn ->
                    if (loggedIn == null) {
                        ProfileUiState(loading = false)
                    } else {
                        ProfileUiState(
                            name = "${loggedIn.firstName} ${loggedIn.lastName}",
                            email = loggedIn.email,
                            initials = loggedIn.firstName.firstOrNull()?.uppercase(),
                            loading = false
                        )
                    }
                }
                .catch { e -> _state.value = ProfileUiState(loading = false, error = e.message) }
                .collect { _state.value = it }
        }
    }
}
