package com.eyehail.mvi_architecture.view


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyehail.mvi_architecture.api.AnimalRepo
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch


import kotlinx.coroutines.flow.collect


class MainViewModel(private val repo: AnimalRepo): ViewModel() {
    val userIntent = Channel<MainIntent>(kotlinx.coroutines.channels.Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    val state: StateFlow<MainState>
        get() = _state
    init {
        handleIntent()
    }


    private fun handleIntent() {
        viewModelScope.launch {
            // need library import kotlinx.coroutines.flow.collect
            userIntent.consumeAsFlow().collect { collector ->
                when (collector) {
                    is MainIntent.FetchAnimals -> fetchAnimals()
                }

            }
        }
    }
    private fun fetchAnimals() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            _state.value = try {
                MainState.Animals(repo.getAnimals())
            } catch(e: Exception) {
                MainState.Error(e.localizedMessage)
            }
        }
    }

}


