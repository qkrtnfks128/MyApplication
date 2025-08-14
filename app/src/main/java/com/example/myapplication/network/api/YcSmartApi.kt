package com.example.myapplication.network.api


import BloodSugarNetworkModel
import BloodSugarNetworkResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface YcSmartApi{
    @POST("inbody/glu")
    suspend fun postBloodSugarData(@Body data: BloodSugarNetworkModel): Response<BloodSugarNetworkResponse>
}
