package com.practice.practicesharedelement

import android.widget.Button
import androidx.compose.animation.DpPropKey
import androidx.compose.animation.animate
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.animation.transition
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SingleScaleAndColorAnimation() {
    val enabled = remember { mutableStateOf(true)}
    val xPosition = if (enabled.value) 40.dp else 60.dp
    val yPosition = if (enabled.value) 40.dp else 60.dp

    Button(
        onClick = { enabled.value = !enabled.value },
        Modifier.offset(
            x = animate(xPosition),
            y = animate(yPosition)
        )
    ) {
        Text("위치 구해오라고!!!!!!!")
    }
}