package xyz.teamgravity.permissionactivityresult

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val permissionDialogs = mutableStateListOf<String>()

    ///////////////////////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////////////////////

    fun onDismissPermissionDialog() {
        permissionDialogs.removeLast()
    }

    fun onPermissionDialogAdd(permission: String) {
        if (!permissionDialogs.contains(permission)) permissionDialogs.add(0, permission)
    }
}