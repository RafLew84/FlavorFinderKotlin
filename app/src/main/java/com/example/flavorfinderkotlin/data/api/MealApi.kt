package com.example.flavorfinderkotlin.data.api

import com.example.flavorfinderkotlin.data.model.MealResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("api/json/v1/1/filter.php?a=Polish")
    suspend fun getFood() : Response<MealResponse>

    @GET("api/json/v1/1/lookup.php?")
    suspend fun getFoodById(@Query("i") id: String) : Response<MealResponse>
}