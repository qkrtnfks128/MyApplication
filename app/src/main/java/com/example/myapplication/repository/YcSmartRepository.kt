package com.example.myapplication.repository


import BloodSugarNetworkModel
import BloodSugarNetworkResponse
import DeviceData
import GLUBodyData
import InBodyGLUDevice
import com.example.myapplication.model.BloodSugarData
import com.example.myapplication.network.NetworkConfig
import com.example.myapplication.network.RetrofitProvider
import com.example.myapplication.network.api.YcSmartApi
import okhttp3.OkHttpClient
import retrofit2.Response


interface YcSmartRepository {
    suspend fun postBloodSugarData(data: BloodSugarData, deviceData: DeviceData): BloodSugarData
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
}

object YcSmartRepositoryFactory {
    fun create(): YcSmartRepositoryImpl {
        val client: OkHttpClient = RetrofitProvider.createOkHttpClient{null}
        val retrofit = RetrofitProvider.createRetrofit(client = client, baseUrl = NetworkConfig.ISENS_BASE_URL)
        val api = retrofit.create(YcSmartApi::class.java)
        return YcSmartRepositoryImpl(api)
    }
}
