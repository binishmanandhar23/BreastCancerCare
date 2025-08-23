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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PermissionViewModel(
    val permissionsController: PermissionsController
) : ViewModel(){

    private val permissionType = Permission.REMOTE_NOTIFICATION
    val permissionState = MutableStateFlow(PermissionState.NotDetermined)

    init {
        viewModelScope.launch {
            permissionState.update { permissionsController.getPermissionState(permissionType) }
            println(permissionState)
        }
    }

    /**
     * An example of using [PermissionsController] in common code.
     */
    fun onRequestPermissionButtonPressed() {
        requestPermission(permissionType)
    }

    private fun requestPermission(permission: Permission) {
        viewModelScope.launch {
            try {
                permissionsController.getPermissionState(permission)
                    .also { println("pre provide $it") }

                // Calls suspend function in a coroutine to request some permission.
                permissionsController.providePermission(permission)
                // If there are no exceptions, permission has been granted successfully.
            } catch (deniedAlwaysException: DeniedAlwaysException) {

            } catch (deniedException: DeniedException) {

            } finally {
                permissionState.update {
                    permissionsController.getPermissionState(permission)
                        .also { println("post provide $it") }
                }
            }
        }
    }

    interface EventListener {

        fun onSuccess()

        fun onDenied(exception: DeniedException)

        fun onDeniedAlways(exception: DeniedAlwaysException)
    }
}