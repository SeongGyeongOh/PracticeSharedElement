package com.practice.practicesharedelement

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.internal.FlowLayout

class MainViewModel: ViewModel() {
    val xOffset = MutableLiveData<Float>()
    val yOffset = MutableLiveData<Float>()
    val width = MutableLiveData<Dp>()
    val height = MutableLiveData<Dp>()
    val index = MutableLiveData<Int>()
    val screenState = MutableLiveData<Boolean>()
}