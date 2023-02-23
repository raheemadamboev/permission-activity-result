package xyz.teamgravity.permissionactivityresult

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen(
    shouldShowRequestPermissionRationale: (String) -> Boolean,
    viewModel: MainViewModel = viewModel(),
) {
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) viewModel.onPermissionDialogAdd(Manifest.permission.CAMERA)
        }
    )

    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            Permissions.value.forEach { if (permissions[it] == false) viewModel.onPermissionDialogAdd(it) }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        ) {
            Text(text = stringResource(id = R.string.request_one_permission))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                permissionsLauncher.launch(Permissions.value)
            }
        ) {
            Text(text = stringResource(id = R.string.request_multiple_permissions))
        }
    }

    viewModel.permissionDialogs.forEach { permission ->
        PermissionDialog(
            regularDescription = when (permission) {
                Manifest.permission.CAMERA -> stringResource(id = R.string.camera_regular_description)
                Manifest.permission.RECORD_AUDIO -> stringResource(id = R.string.microphone_regular_description)
                Manifest.permission.CALL_PHONE -> stringResource(id = R.string.phone_call_regular_permission)
                else -> return@forEach
            },
            permanentlyDeclinedDescription = stringResource(
                id = R.string.permanently_declined_description,
                when (permission) {
                    Manifest.permission.CAMERA -> stringResource(id = R.string.camera)
                    Manifest.permission.RECORD_AUDIO -> stringResource(id = R.string.microphone)
                    Manifest.permission.CALL_PHONE -> stringResource(id = R.string.phone_calling)
                    else -> return@forEach
                }
            ),
            permanentlyDeclined = !shouldShowRequestPermissionRationale(permission),
            onDismiss = viewModel::onDismissPermissionDialog,
            onConfirm = {
                viewModel.onDismissPermissionDialog()
                permissionsLauncher.launch(arrayOf(permission))
            }
        )
    }
}