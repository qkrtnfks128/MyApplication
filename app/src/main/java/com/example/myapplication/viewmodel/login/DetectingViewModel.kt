package com.example.myapplication.viewmodel.login

import android.graphics.Bitmap
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.utils.WEvent
import com.example.myapplication.utils.WCV
import com.example.myapplication.utils.ConvertUtils
//import com.example.myapplication.utils.PreferenceHelper
import com.onethefull.wonderful_cv_library.CV_Package.FaceDetectionAsyncTask
import com.onethefull.wonderful_cv_library.CV_Package.WonderfulCV
import timber.log.Timber
import android.os.Handler
import android.os.Looper
 
class DetectingViewModel  : ViewModel() {

    private val wonderfulCV: WonderfulCV by lazy { WCV.getInstance().wonderfulCV }

    private var cnt = 0

    private val _checking = MutableLiveData<Boolean>()
    val checking: LiveData<Boolean> = _checking

    private val _dataLoading = MutableLiveData<Boolean>().apply { value = false }
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _toastEvent = MutableLiveData<WEvent<String>>()
    val toastEvent: LiveData<WEvent<String>> = _toastEvent

    private val _backEvent = MutableLiveData<WEvent<Unit>>()
    val backEvent: LiveData<WEvent<Unit>> = _backEvent


    fun backEvent() {
        _backEvent.value = WEvent(Unit)
    }

    private var imageCounter = 0
    fun checkFace(face: Bitmap) {
        try {
            imageCounter++
            Timber.d(imageCounter.toString())
            if (imageCounter < 30) return
            imageCounter = 0
            val faceDetectionTask = FaceDetectionAsyncTask { faceList ->
                if (!faceList.isNullOrEmpty() || !faceList[0].equals("Unknown")) {
                    val currentFace = faceList[0]
                    
//                    PreferenceHelper.cvid = currentFace.userId
//                    PreferenceHelper.children_name = currentFace.firstName
//                    PreferenceHelper.children_image = ConvertUtils.bitmapToString(face)
                    _checking.value = true
                } else {
                    Timber.d("실패 :: $cnt")
                    if (cnt == 6) {
                        cnt = 0
                        _toastEvent.value = WEvent("누구인지 확인 못하겠어요.. 얼굴각도를 다르게 한번해보아요.")
                    } else {
                        cnt++
                        _checking.value = false
                    }
                }
            }

            //test
            _checking.value = true
            
            Handler(Looper.getMainLooper()).postDelayed({
                _checking.value = false
            }, 1000)
            // return

            faceDetectionTask.setConnectionInfo(
                wonderfulCV.token,
                wonderfulCV.serverAddress + "/api/user/detection",
                face
            )

            faceDetectionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.e(e)
            
            _toastEvent.value = WEvent("등록된 얼굴이 없어요.")
        }

    }

}