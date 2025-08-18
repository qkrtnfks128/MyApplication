package com.example.myapplication.network.api


import BloodPressureNetworkModel
import BloodPressureNetworkResponse
import BloodSugarNetworkModel
import BloodSugarNetworkResponse
import WeightNetworkModel
import WeightNetworkResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface YcSmartApi{
    @POST("blood/sugar/insert")
    suspend fun postBloodSugarData(@Body data: BloodSugarNetworkModel): Response<BloodSugarNetworkResponse>

    @POST("blood/press/insert")
    suspend fun postBloodPressureData(@Body data: BloodPressureNetworkModel): Response<BloodPressureNetworkResponse>

    @POST("body/comp/insert")
    suspend fun postWeightData(@Body data: WeightNetworkModel): Response<WeightNetworkResponse>

}
