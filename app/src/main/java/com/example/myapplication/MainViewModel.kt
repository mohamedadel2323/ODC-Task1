package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _liveData = MutableLiveData<String>()
    val liveData: LiveData<String>
        get() = _liveData
    private val _stateFlow = MutableStateFlow("Hello World!")
    val stateFlow = _stateFlow.asStateFlow()
    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun changeLiveDataValue(value: String) { _liveData.value = value }
    fun changeStateFlowValue(value: String) { _stateFlow.value = value }
    fun changeSharedFlowValue(value: String) { viewModelScope.launch { _sharedFlow.emit(value) } }
    fun changeFlowValue(): Flow<String> = flow {
        repeat(5) {
            emit(it.toString())
            delay(1000)
        }
    }
}