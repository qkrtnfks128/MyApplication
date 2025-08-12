package com.example.myapplication.page.login

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.camera.core.Preview as CameraPreview
import androidx.compose.runtime.DisposableEffect
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import android.graphics.Bitmap
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.login.DetectingViewModel
import com.example.myapplication.manager.SelectedOrgStore
import com.example.myapplication.model.UserListItem
import com.example.myapplication.model.UserListResult
import com.example.myapplication.navigation.Screen
import com.example.myapplication.utils.WCV
import com.example.myapplication.utils.LogManager

@Composable
fun DetectingScreen(
    navController: NavController,
) {
    val viewModel: DetectingViewModel = viewModel()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var statusText by remember { mutableStateOf("얼굴을 카메라에 맞춰주세요") }
    var hasCameraPermission by remember { mutableStateOf(false) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    val isChecking: Boolean by viewModel.checking.observeAsState(false)
    val toastEvent = viewModel.toastEvent.observeAsState().value

    // ML Kit FaceDetector (하나만 생성하여 재사용)
    val detectorOptions = remember {
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            .enableTracking()
            .build()
    }
    val faceDetector: FaceDetector = remember { FaceDetection.getClient(detectorOptions) }
    DisposableEffect(Unit) {
        onDispose {
            faceDetector.close()
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    

    LaunchedEffect(Unit) {
      LogManager.info("WCV", "init");
      val org = SelectedOrgStore.getSelected()
      if (org != null) {
        WCV.getInstance()
          .init(org.orgUuid, org.password)
      }
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) {
            hasCameraPermission = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
        
    }

    LaunchedEffect(hasCameraPermission) {
        if (hasCameraPermission) {
            val future = ProcessCameraProvider.getInstance(context)
            future.addListener(
                { cameraProvider = future.get() },
                ContextCompat.getMainExecutor(context)
            )
        }
    }

    LaunchedEffect(isChecking) {
        if (isChecking) {
            statusText = "얼굴 인식 완료!"
            navController.currentBackStackEntry?.savedStateHandle?.set(
                Screen.UserResult.KEY_RESULT,
                UserListResult(items = listOf(UserListItem(cvid = "24820", name = "박수란")), statusCode = 200,)
            )
            navController.navigate(Screen.UserResult.route)
        }
    }

    LaunchedEffect(toastEvent) {
        toastEvent?.getContentIfNotHandled()?.let { message ->
            android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(30.dp)
                .size(84.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text(text = "←", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }

        Text(
            text = if (hasCameraPermission) statusText else "카메라 권한이 필요합니다",
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 120.dp)
        )

        if (hasCameraPermission && cameraProvider != null) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        val provider = cameraProvider ?: return@apply
                        val preview = CameraPreview.Builder().build()
                        val analysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .apply {
                                setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                                    val mediaImage = imageProxy.image
                                    if (mediaImage == null) {
                                        imageProxy.close()
                                        return@setAnalyzer
                                    }
                                    val image = InputImage.fromMediaImage(
                                        mediaImage,
                                        imageProxy.imageInfo.rotationDegrees
                                    )
                                    faceDetector
                                        .process(image)
                                        .addOnSuccessListener { faces ->
                                            if (faces.isNotEmpty()) {
                                                // 성공 프레임을 주기적으로만 반영하도록 ViewModel 내부에서 디바운스
                                                val dummy = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
                                                viewModel.checkFace(dummy)
                                            } else {
                                                statusText = "얼굴을 카메라에 맞춰주세요"
                                            }
                                        }
                                        .addOnFailureListener {
                                            // 무시하고 다음 프레임
                                        }
                                        .addOnCompleteListener {
                                            imageProxy.close()
                                        }
                                }
                            }
                        provider.unbindAll()
                        provider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_FRONT_CAMERA,
                            preview,
                            analysis
                        )
                        preview.setSurfaceProvider(surfaceProvider)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .padding(top = 180.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetectingScreenPreview() {
    val nav = rememberNavController()
    DetectingScreen(nav)
}