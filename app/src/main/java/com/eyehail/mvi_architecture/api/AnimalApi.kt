package com.eyehail.mvi_architecture.api

import com.eyehail.mvi_architecture.model.Animal
import retrofit2.http.GET

interface AnimalApi {
    @GET("animals.json")
    suspend fun getAnimals(): List<Animal>
}