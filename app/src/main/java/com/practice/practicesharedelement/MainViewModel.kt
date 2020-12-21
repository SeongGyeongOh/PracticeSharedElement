package com.practice.practicesharedelement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val _screenState = MutableLiveData<Boolean>()
    val screenState: LiveData<Boolean> get() = _screenState

    init {
        _screenState.value = true
    }
}