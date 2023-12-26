package com.example.fundate.notification.api

import com.example.fundate.notification.model.PushNotification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAAFLU6pfw:APA91bF143d7fgZhG1m9n40hf8diKXjI2CQtCZvTp2W5mqWcZzMocDmdJq3AWplqdDqK6TKWueAnPm3tLe3soOeATP0wFzAL0-6TXu1h2wRfJDLPPH8ilqH4rXqRIYk3FTnwD1wm2GMn"
    )
    @POST("fcm/send")
    fun sendNotification(@Body notification: PushNotification): Call<PushNotification>
}