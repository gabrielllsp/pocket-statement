package com.gabriel.pocketstatement.ui.screen.camera

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.gabriel.pocketstatement.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    onNavigateBack: () -> Unit, // Adicionamos onNavigateBack
    onPhotoCaptured: (Uri) -> Unit // Mantemos onPhotoCaptured com Uri
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
    onPhotoCaptured: (Uri) -> Unit
) {
    val context: Context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply { this.controller = cameraController }
            },
            modifier = Modifier.fillMaxSize()
        )
        // Garante que o controlador está ligado ao ciclo de vida
        LaunchedEffect(Unit) {
            cameraController.bindToLifecycle(lifecycleOwner)
        }

        IconButton(
            onClick = {
                val outputFile = createTempImageFile(context)
                val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()

                cameraController.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            val savedUri = outputFileResults.savedUri ?: Uri.fromFile(outputFile)
                            onPhotoCaptured(savedUri)
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e("CameraView", "Error capturing image: ${exception.message}", exception)
                        }
                    }
                )
            },
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = "Take picture", modifier = Modifier.size(48.dp))
        }
    }
}

// Função auxiliar para criar um arquivo temporário para a imagem
private fun createTempImageFile(context: Context): File {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir: File? = context.externalCacheDir
    return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
}

@Composable
private fun PermissionDeniedContent(
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