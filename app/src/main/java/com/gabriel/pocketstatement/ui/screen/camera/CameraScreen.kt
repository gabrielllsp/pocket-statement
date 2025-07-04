package com.gabriel.pocketstatement.ui.screen.camera

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.gabriel.pocketstatement.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale




@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    onNavigateBack: () -> Unit,
    onPhotoCaptured: (Bitmap) -> Unit
) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    Scaffold { paddingValues ->
        when (cameraPermissionState.status) {
            PermissionStatus.Granted -> {
                CameraView(
                    modifier = Modifier.padding(paddingValues),
                    onPhotoCaptured = onPhotoCaptured
                )
            }

            is PermissionStatus.Denied -> {
                PermissionDeniedContent(
                    shouldShowRationale = cameraPermissionState.status.shouldShowRationale,
                    onRequestPermission = { cameraPermissionState.launchPermissionRequest() },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}


@Composable
fun CameraView(
    modifier: Modifier = Modifier,
    onPhotoCaptured: (Bitmap) -> Unit
) {
    val context: Context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    // Link the PreviewView to the LifecycleCameraController
                    controller = cameraController
                    // You might need to set scaleType depending on your needs
                    // scaleType = PreviewView.ScaleType.FILL_CENTER
                }
            },
            modifier = Modifier.fillMaxSize(),
            // Optional: If you need to update the controller or other properties
            update = { previewView ->
                // Ensure the controller is bound to the lifecycle owner
                cameraController.bindToLifecycle(lifecycleOwner)
                previewView.controller = cameraController // Re-assign in case of recomposition
            }
        )

        IconButton(
            onClick = {
                val executor = ContextCompat.getMainExecutor(context)
                cameraController.takePicture(
                    executor,
                    object : ImageCapture.OnImageCapturedCallback() {
                        override fun onCaptureSuccess(image: ImageProxy) {
                            val capturedBitmap = image.toBitmap()
                            image.close()
                            onPhotoCaptured(capturedBitmap)
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e(
                                "CameraView",
                                "Error capturing image: ${exception.message}",
                                exception
                            )
                        }
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PhotoCamera,
                contentDescription = "Take picture",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun PermissionDeniedContent(
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val textToShow = if (shouldShowRationale) {
            stringResource(R.string.camera_permission_rationale)
        } else {
            stringResource(R.string.camera_permission_required)
        }

        Text(text = textToShow, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Text(text = stringResource(R.string.grant_permission_button))
        }
    }
}