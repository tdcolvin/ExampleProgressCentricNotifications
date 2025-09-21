package com.tdcolvin.exampleprogresscentricnotifications

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@SuppressLint("ComposableNaming")
@Composable
fun rememberPermissionScope(permission: String): MutableState<PermissionScope> {
    val context = LocalContext.current

    val permissionGranted = remember {
        mutableStateOf(context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted.value = granted
    }

    return remember(permission, permissionGranted, launcher) {
        mutableStateOf(PermissionScope(
            permission = permission,
            permissionGranted = permissionGranted,
            launcher = launcher,
            requestPermission = {
                launcher.launch(permission)
            }
        ))
    }
}

data class PermissionScope(
    val permission: String,
    val permissionGranted: MutableState<Boolean>,
    val launcher: ManagedActivityResultLauncher<String, Boolean>,
    val requestPermission: () -> Unit
)

@Composable
fun WithPermission(
    permission: String,
    permissionRequiredContent: @Composable PermissionScope.() -> Unit = { DefaultRequestPermissionScreen() },
    permissionGrantedContent: @Composable PermissionScope.() -> Unit = { }
) {
    var permissionScope by rememberPermissionScope(permission)

    with (permissionScope) {
        if (!permissionGranted.value) {
            permissionRequiredContent()
        }
        else {
            permissionGrantedContent()
        }
    }
}

@Composable
fun PermissionScope.DefaultRequestPermissionScreen() {
    Button(modifier = Modifier.padding(top = 20.dp), onClick = requestPermission) {
        Text("Request permission")
    }
}

@Composable
fun MyScreen() = WithPermission(Manifest.permission.POST_NOTIFICATIONS, {
}, { Text("Permission granted") })