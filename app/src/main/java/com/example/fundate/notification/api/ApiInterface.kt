package com.example.fundate.notification.api

import com.example.fundate.BuildConfig
import com.example.fundate.notification.model.PushNotification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers(
        "Content-Type:application/json",
        "Authorization:key=${BuildConfig.API_KEY}"
    )
    @POST("fcm/send")
    fun sendNotification(@Body notification: PushNotification): Call<PushNotification>
}