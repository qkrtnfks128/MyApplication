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
import com.example.myapplication.model.BloodPressureHistoryData
import com.example.myapplication.model.BloodSugarData
import com.example.myapplication.model.BloodSugarHistoryData
import com.example.myapplication.model.WeightData
import com.example.myapplication.model.WeightHistoryData
import com.example.myapplication.network.ApiResult
import com.example.myapplication.network.NetworkConfig
import com.example.myapplication.network.RetrofitProvider
import com.example.myapplication.network.dto.ycsmart.BloodPressureHistoryResponse
import com.example.myapplication.network.dto.ycsmart.BloodSugarHistoryResponse
import com.example.myapplication.network.dto.ycsmart.WeightHistoryResponse
import com.example.myapplication.network.api.YcSmartApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Response


interface YcSmartRepository {
    suspend fun postBloodSugarData(data: BloodSugarData, deviceData: DeviceData): BloodSugarData
    suspend fun postBloodPressureData(data: BloodPressureData, deviceData: DeviceData): BloodPressureData
    suspend fun postWeightData(data: WeightData, deviceData: DeviceData): WeightData

    /**
     * 혈당 기록 목록을 페이징하여 조회
     * @param userId 사용자 ID
     * @param offset 건너뛰기 개수
     * @param limit 가져올 데이터 개수
     * @return 혈당 기록 목록
     */
    suspend fun getBloodSugarHistory(userId: String, offset: Int, limit: Int): BloodSugarHistoryData

    /**
     * 혈압 기록 목록을 페이징하여 조회
     * @param userId 사용자 ID
     * @param offset 건너뛰기 개수
     * @param limit 가져올 데이터 개수
     * @return 혈압 기록 목록
     */
    suspend fun getBloodPressureHistory(userId: String, offset: Int, limit: Int): BloodPressureHistoryData

    /**
     * 체중 기록 목록을 페이징하여 조회
     * @param userId 사용자 ID
     * @param offset 건너뛰기 개수
     * @param limit 가져올 데이터 개수
     * @return 체중 기록 목록
     */
    suspend fun getWeightHistory(userId: String, offset: Int, limit: Int): WeightHistoryData
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

    override suspend fun getBloodSugarHistory(userId: String, offset: Int, limit: Int): BloodSugarHistoryData {
        val response: Response<BloodSugarHistoryResponse> = ycSmartApi.getBloodSugarHistory(userId, offset, limit)
        // 인터셉터에서 오류 처리를 하민로 여기서는 오류 처리를 하지 않음
        // 응답이 성공적이면 데이터를 반환하고, 아니면 빈 목록 반환
        return BloodSugarHistoryData(
            hasNext = response.body()?.hasNext ?: false,
            list = response.body()?.list ?: emptyList()
        )
    }

    override suspend fun getBloodPressureHistory(userId: String, offset: Int, limit: Int): BloodPressureHistoryData {
        val response: Response<BloodPressureHistoryResponse> = ycSmartApi.getBloodPressureHistory(userId, offset, limit)
        // 인터셉터에서 오류 처리를 하민로 여기서는 오류 처리를 하지 않음
        // 응답이 성공적이면 데이터를 반환하고, 아니면 빈 목록 반환
        return BloodPressureHistoryData(
            hasNext = response.body()?.hasNext ?: false,
            list = response.body()?.list ?: emptyList()
        )
    }

    override suspend fun getWeightHistory(userId: String, offset: Int, limit: Int): WeightHistoryData {
        val response: Response<WeightHistoryResponse> = ycSmartApi.getWeightHistory(userId, offset, limit)
        // 인터셉터에서 오류 처리를 하민로 여기서는 오류 처리를 하지 않음
        // 응답이 성공적이면 데이터를 반환하고, 아니면 빈 목록 반환
        return WeightHistoryData(
            hasNext = response.body()?.hasNext ?: false,
            list = response.body()?.list ?: emptyList()
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
