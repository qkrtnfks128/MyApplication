package com.example.myapplication.repository


import BPBodyData
import BloodPressureNetworkModel
import BloodPressureNetworkResponse
import BloodSugarNetworkModel
import BloodSugarNetworkResponse
import DeviceData
import GLUBodyData
import InBodyBpDevice
import InBodyGLUDevice
import ScaleBodyData
import SevenElecScaleDevice
import WeightNetworkModel
import WeightNetworkResponse
import com.example.myapplication.model.BloodPressureData
import com.example.myapplication.model.BloodSugarData
import com.example.myapplication.model.WeightData
import com.example.myapplication.network.NetworkConfig
import com.example.myapplication.network.RetrofitProvider
import com.example.myapplication.network.api.YcSmartApi
import okhttp3.OkHttpClient
import retrofit2.Response


interface YcSmartRepository {
    suspend fun postBloodSugarData(data: BloodSugarData, deviceData: DeviceData): BloodSugarData
    suspend fun postBloodPressureData(data: BloodPressureData, deviceData: DeviceData): BloodPressureData
    suspend fun postWeightData(data: WeightData, deviceData: DeviceData): WeightData
}

class YcSmartRepositoryImpl(
    private val ycSmartApi: YcSmartApi
) : YcSmartRepository {
    override suspend fun postBloodSugarData(data: BloodSugarData, deviceData: DeviceData): BloodSugarData {
        val networkModel:BloodSugarNetworkModel = BloodSugarNetworkModel(
            device_name = deviceData.deviceName,
            device_address = deviceData.deviceAddress,
            device_type = deviceData.deviceType,
            cvId = deviceData.cvId,
            tel = deviceData.tel,
            connect = deviceData.connect,
            regist = deviceData.regist,
            datetime = deviceData.datetime,
            data = InBodyGLUDevice(
                body_data = GLUBodyData(
                    Date = data.date,
                    Time = data.time,
                    GlucoseResult = data.glucoseResult,
                    Temperature = data.temperature
                )
            )
        )
        val response: Response<BloodSugarNetworkResponse> = ycSmartApi.postBloodSugarData(networkModel)
        return BloodSugarData(
            date = data.date,
            time = data.time,
            glucoseResult = data.glucoseResult,
            temperature = data.temperature,
            mealFlag = data.mealFlag,
            judgment = response.body()?.judgment ?: null
        )
    }

    override suspend fun postBloodPressureData(data: BloodPressureData, deviceData: DeviceData): BloodPressureData {
        val networkModel:BloodPressureNetworkModel = BloodPressureNetworkModel(
            device_name = deviceData.deviceName,
            device_address = deviceData.deviceAddress,
            device_type = deviceData.deviceType,
            cvId = deviceData.cvId,
            tel = deviceData.tel,
            connect = deviceData.connect,
            regist = deviceData.regist,
            datetime = deviceData.datetime,
            data = InBodyBpDevice(
                body_data = BPBodyData(
                    Date = data.date,
                    Time = data.time,
                    SBP = data.systolic,
                    DBP = data.diastolic,
                    HR = data.pulse
                )
            )
        )
        val response: Response<BloodPressureNetworkResponse> = ycSmartApi.postBloodPressureData(networkModel)
        return BloodPressureData(
            date = data.date,
            time = data.time,
            systolic = data.systolic,
            diastolic = data.diastolic,
            pulse = data.pulse,
            judgment = response.body()?.judgment ?: null
        )
    }

    override suspend fun postWeightData(data: WeightData, deviceData: DeviceData): WeightData {
        val networkModel:WeightNetworkModel = WeightNetworkModel(
            device_name = deviceData.deviceName,
            device_address = deviceData.deviceAddress,
            device_type = deviceData.deviceType,
            cvId = deviceData.cvId,
            tel = deviceData.tel,
            connect = deviceData.connect,
            regist = deviceData.regist,
            datetime = deviceData.datetime,
            data = SevenElecScaleDevice(
                body_data = ScaleBodyData(
                    Date = data.date,
                    Time = data.time,
                    scale = data.scale,
                    bmi = data.bmi,
                    fatRate = data.fatRate,
                    muscleRate = data.muscleRate
                )
            )
        )
        val response: Response<WeightNetworkResponse> = ycSmartApi.postWeightData(networkModel)
        return WeightData(
            date = data.date,
            time = data.time,
            scale = data.scale,
            bmi = data.bmi,
            fatRate = data.fatRate,
            muscleRate = data.muscleRate,
            judgment = response.body()?.judgment ?: null
        )
    }
}

object YcSmartRepositoryFactory {
    fun create(): YcSmartRepositoryImpl {
        val client: OkHttpClient = RetrofitProvider.createOkHttpClient{null}
        val retrofit = RetrofitProvider.createRetrofit(client = client, baseUrl = NetworkConfig.ISENS_BASE_URL)
        val api = retrofit.create(YcSmartApi::class.java)
        return YcSmartRepositoryImpl(api)
    }
}
