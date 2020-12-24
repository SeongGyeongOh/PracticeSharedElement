package com.practice.practicesharedelement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.*
import androidx.compose.animation.transition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.geometry.Offset.Companion.Infinite
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.practice.practicesharedelement.ui.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setContent {
            AnimatedFavButton()
        }
    }
}

enum class ButtonState {
    IDLE, PRESSED
}

@Composable
fun AnimatedFavButton() {
    val buttonState = remember{ mutableStateOf(ButtonState.IDLE) }

    val transitionDefinition = transitionDefinition<ButtonState> {
        state(ButtonState.IDLE) {
            this[width] = 300.dp
            this[roundedCorners] = 6
            this[backgroundColor] = purple500
            this[textColor] = Color.White
            this[textOpacity] = 1f
            this[iconOpacity] = 0f
            this[pressedHeartSize] = 48.dp
            this[idleHeartSize] = 24.dp
        }

        state(ButtonState.PRESSED) {
            this[width] = 60.dp
            this[roundedCorners] = 50
            this[backgroundColor] = Color.White
            this[textColor] = purple500
            this[textOpacity] = 0f
            this[iconOpacity] = 1f
            this[pressedHeartSize] = 48.dp
            this[idleHeartSize] = 24.dp
        }

        transition(ButtonState.IDLE, ButtonState.PRESSED) {
            width using tween(1500)
            roundedCorners using tween(
                durationMillis = 1500,
                easing = FastOutLinearInEasing
            )
            backgroundColor using tween(3000)
            textColor using tween(500)
            textOpacity using tween(1500)
            iconOpacity using tween(1500)
            pressedHeartSize using keyframes {
                durationMillis = 1500
                48.dp at 900
                12.dp at 1300
            }
        }

        transition(ButtonState.PRESSED to ButtonState.IDLE) {
            width using tween(1500)
            roundedCorners using tween (
                durationMillis = 1500,
                easing = FastOutLinearInEasing
            )
            backgroundColor using tween(3000)
            textColor using tween(500)
            textOpacity using tween(3000)
            iconOpacity using tween(3000)
            idleHeartSize using repeatable (
                animation = keyframes {
                    durationMillis = 2000
                    24.dp at 1400
                    12.dp at 1500
                    24.dp at 1600
                    12.dp at 1700
                }, iterations = 5
            )
        }
    }
    val toState = if(buttonState.value == ButtonState.IDLE) {
        ButtonState.PRESSED
    } else {
        ButtonState.IDLE
    }

    val state = transition(
        definition = transitionDefinition,
        initState = buttonState.value,
        toState = toState
    )

    FavButton(buttonState = buttonState, state = state)
}

@Composable
fun FavButton(buttonState: MutableState<ButtonState>, state: TransitionState) {
    Button(
            border = BorderStroke(1.dp, purple500),
            colors = ButtonDefaults.buttonColors(backgroundColor = state[backgroundColor]),
            shape = RoundedCornerShape(state[roundedCorners]),
            modifier = Modifier.size(state[width], 60.dp),
            onClick = {
                buttonState.value = if(buttonState.value == ButtonState.IDLE) {
                    ButtonState.PRESSED
                } else {
                    ButtonState.IDLE
                }
            }
    ) {
        ButtonContent(buttonState, state)
    }
}

@Composable
fun ButtonContent(buttonState: MutableState<ButtonState>, state: TransitionState) {
    if(buttonState.value == ButtonState.PRESSED) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.width(24.dp), Arrangement.Center) {
                Icon(
                        tint = state[textColor],
                        imageVector = Icons.Default.FavoriteBorder,
                        modifier = Modifier.size(state[idleHeartSize])
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                    "ADD TO FAVORITES!",
                    softWrap = false,
                    color = state[textColor],
                    modifier = Modifier.alpha(state[textOpacity])
            )
        }
    } else {
        Icon(
                tint = state[textColor],
                imageVector = Icons.Default.Favorite,
                modifier = Modifier.size(state[pressedHeartSize]).alpha(state[iconOpacity]) //4
        )
    }

}
