package com.example.myapplication.network.api


import BloodPressureNetworkModel
import BloodPressureNetworkResponse
import BloodSugarNetworkModel
import BloodSugarNetworkResponse
import WeightNetworkModel
import WeightNetworkResponse
import com.example.myapplication.model.BloodSugarData
import com.example.myapplication.network.dto.ycsmart.BloodSugarHistoryResponse
import com.example.myapplication.network.dto.ycsmart.PageInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface YcSmartApi{
    @POST("blood/sugar/insert")
    suspend fun postBloodSugarData(@Body data: BloodSugarNetworkModel): Response<BloodSugarNetworkResponse>

    @POST("blood/press/insert")
    suspend fun postBloodPressureData(@Body data: BloodPressureNetworkModel): Response<BloodPressureNetworkResponse>

    @POST("body/comp/insert")
    suspend fun postWeightData(@Body data: WeightNetworkModel): Response<WeightNetworkResponse>

    /**
     * 혈당 기록 목록을 페이징하여 조회
     * @param userId 사용자 ID
     * @param offset 건너뛰기 개수
     * @param limit 가져올 데이터 개수
     * @return 혈당 기록 목록
     */
    @GET("blood/sugar/history")
    suspend fun getBloodSugarHistory(
        @Query("userId") userId: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<BloodSugarHistoryResponse>
}
