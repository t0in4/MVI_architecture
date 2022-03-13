package com.eyehail.mvi_architecture.view

sealed class MainIntent {

    object FetchAnimals: MainIntent()
}