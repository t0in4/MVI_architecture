package com.eyehail.mvi_architecture.api

class AnimalRepo(private val api: AnimalApi) {
    suspend fun getAnimals() = api.getAnimals()
}