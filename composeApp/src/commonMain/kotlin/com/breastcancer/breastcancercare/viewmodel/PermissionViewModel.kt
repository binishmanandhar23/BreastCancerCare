package com.breastcancer.breastcancercare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PermissionViewModel(
    val permissionsController: PermissionsController
) : ViewModel(){

    private val permissionType = Permission.REMOTE_NOTIFICATION
    private var _permissionState = MutableStateFlow(PermissionState.NotDetermined)
    val permissionState = _permissionState.asStateFlow()

    private var _permissionImportantDialog = MutableStateFlow(false)
    val permissionImportantDialog = _permissionImportantDialog.asStateFlow()

    init {
        viewModelScope.launch {
            _permissionState.update { permissionsController.getPermissionState(permissionType) }
        }
    }


    fun onRequestPermissionButtonPressed() {
        requestPermission(permissionType)
    }

    fun showDialog() = _permissionImportantDialog.update { true }
    fun dismissDialog() = _permissionImportantDialog.update { false }

    private fun requestPermission(permission: Permission) {
        viewModelScope.launch {
            try {
                permissionsController.getPermissionState(permission)

                // Calls suspend function in a coroutine to request some permission.
                permissionsController.providePermission(permission)
                // If there are no exceptions, permission has been granted successfully.
            } catch (deniedAlwaysException: DeniedAlwaysException) {

            } catch (deniedException: DeniedException) {

            } finally {
                _permissionState.update {
                    permissionsController.getPermissionState(permission)
                }
            }
        }
    }
}