package com.practice.practicesharedelement

import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.practice.practicesharedelement.navigation.Actions
import com.practice.practicesharedelement.navigation.AmbientBackDispatcher
import com.practice.practicesharedelement.navigation.Destination
import com.practice.practicesharedelement.navigation.Navigator
import com.practice.practicesharedelement.ui.PracticeSharedElementTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PracticeSharedElementTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    RootScreen(
                            backDispatcher = onBackPressedDispatcher
                    )
                }
            }
        }
    }
}

@Composable
fun RootScreen(backDispatcher: OnBackPressedDispatcher) {
    val navigator: Navigator<Destination> = rememberSavedInstanceState(
        saver = Navigator.saver(backDispatcher)
    ) {
        Navigator(Destination.FirstScreen, backDispatcher)
    }
    val actions = remember(navigator) { Actions(navigator) }

    Box(Modifier.fillMaxSize()) {
        Providers(AmbientBackDispatcher provides backDispatcher) {
            Crossfade(current = navigator.current) { destination ->
                when(destination) {
                    is Destination.FirstScreen -> {
                        Screen1(
                            moveToNextScreen = actions.goToSecond
                        )
                    }
                    is Destination.SecondScreen -> {
                        Screen2(
                                destination,
                                backDispatcher
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Screen1(
        moveToNextScreen: (item: User, offset: Offset, sizeList: List<Dp>) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        WithConstraints() {
            LazyColumn(Modifier.fillMaxWidth()) {
                items(tempList) { item ->
                    ListItem(
                        moveToNextScreen = moveToNextScreen,
                        item = item,
                    )
                }
            }
        }
    }
}

@Composable
fun Screen2(
        item: Destination.SecondScreen,
        backDispatcher: OnBackPressedDispatcher,
) {
    WithConstraints {
        val screenState = remember { mutableStateOf(false) }
        val xOffset = remember { mutableStateOf(item.offset.x)}
        val yOffset = remember { mutableStateOf(item.offset.y) }
        val width = remember { mutableStateOf(item.list[0])}
        val height = remember { mutableStateOf(item.list[1])}
        val alpha = remember { mutableStateOf(0f)}
        if(screenState.value) {
            xOffset.value = 0f
            yOffset.value = 0f
            width.value = with(AmbientDensity.current) {constraints.maxWidth.toDp()}
            height.value = 300.dp
            alpha.value = 1f
        }

        onActive(callback = {
            screenState.value = true
        })

        backDispatcher.onBackPressed(

        )


        Box(
            Modifier.fillMaxSize()
                    .background(Color.Gray),
        ) {
            Image(
                imageResource(id = item.user.img),
                Modifier.preferredWidth(animate(target = width.value, animSpec = tween(1000)))
                        .preferredHeight(animate(target = height.value, animSpec = tween(1000)))
                        .offset(
                                x = animate(
                                        target = xOffset.value.dp,
                                        animSpec = tween(1000)
                                ),
                                y = animate(
                                        target = yOffset.value.dp,
                                        animSpec = tween(1000)
                                )
                        )
                        .alpha(animate(target = alpha.value, animSpec = tween(1000)))
            )
            Text(item.user.name)
        }
    }
}


@Composable
fun ListItem(
        moveToNextScreen: (item: User, offset: Offset, sizeList: List<Dp>) -> Unit,
        item: User,
) {
    WithConstraints {
        val height = 70.dp
        val width = 70.dp

        val offset = remember { mutableStateOf(Offset(0))}

        Box(Modifier.fillMaxSize()) {
            Row(
                Modifier.fillMaxWidth()
                        .preferredHeight(height = height)
                        .preferredWidth(width)
                        .clickable(onClick = {
                            moveToNextScreen(item, offset.value, listOf(height, width))
                        })
            )
            {
                Image(
                    imageResource(id = item.img),
                    Modifier.preferredSize(50.dp)
                            .onGloballyPositioned {
                                offset.value = it.positionInRoot.div(3f)
                            }
                )
                Text(item.name)
            }
        }
    }
}

data class User(val name: String, val img: Int)

val tempList = listOf(
    User("멍멍1", R.drawable.img_temp),
    User("멍멍2", R.drawable.img_temp),
    User("멍멍3", R.drawable.img_temp),
    User("멍멍4", R.drawable.img_temp),
    User("멍멍5", R.drawable.img_temp),
    User("멍멍6", R.drawable.img_temp),
    User("멍멍7", R.drawable.img_temp),
    User("멍멍8", R.drawable.img_temp),
    User("멍멍9", R.drawable.img_temp),
)