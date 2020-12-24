package com.practice.practicesharedelement

import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.practicesharedelement.navigation.Actions
import com.practice.practicesharedelement.navigation.AmbientBackDispatcher
import com.practice.practicesharedelement.navigation.Destination
import com.practice.practicesharedelement.navigation.Navigator
import com.practice.practicesharedelement.ui.PracticeSharedElementTheme

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setContent {
            PracticeSharedElementTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    RootScreen(
                            backDispatcher = onBackPressedDispatcher,
                            mainViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun RootScreen(
        backDispatcher: OnBackPressedDispatcher,
        mainViewModel: MainViewModel
) {
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
                            moveToNextScreen = actions.goToSecond,
                            mainViewModel
                        )
                    }
                    is Destination.SecondScreen -> {
                        Screen2(
                                destination,
                                backDispatcher,
                                mainViewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Screen1(
        moveToNextScreen: (item: User, offset: Offset, sizeList: List<Dp>) -> Unit,
        mainViewModel: MainViewModel
) {
    Column(Modifier.fillMaxSize()) {
        WithConstraints() {
            LazyColumn(Modifier.fillMaxWidth()) {
                itemsIndexed(tempList) { index, item ->
                    ListItem(moveToNextScreen = moveToNextScreen,
                            item = item,
                            itemIndex = index,
                            mainViewModel = mainViewModel
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
        mainViewModel: MainViewModel,
) {
    WithConstraints {
        val screenState = remember { mutableStateOf(false) }
        val xOffset = remember { mutableStateOf(item.offset.x)}
        val yOffset = remember { mutableStateOf(item.offset.y) }
        val width = remember { mutableStateOf(item.list[0])}
        val height = remember { mutableStateOf(item.list[1])}
        val alpha = remember { mutableStateOf(0f)}

        if(screenState.value) {
            xOffset.value = 50f
            yOffset.value = 0f
            width.value = with(AmbientDensity.current) {constraints.maxWidth.toDp()}
            height.value = 300.dp
            alpha.value = 1f

            mainViewModel.xOffset.value = xOffset.value
            mainViewModel.yOffset.value = yOffset.value
            mainViewModel.width.value = width.value
            mainViewModel.height.value = height.value
        }

        onActive(callback = {
            screenState.value = true
        })

        Box(
            Modifier.fillMaxSize()
                    .background(Color.Gray),
        ) {
            Image(
                    imageResource(id = item.user.img),
                    Modifier.preferredWidth(animate(target = width.value, animSpec = tween(1000)))
                            .preferredHeight(animate(target = height.value, animSpec = tween(1000)))
                            .offset (
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
                            .onGloballyPositioned {
                                mainViewModel.xOffset.value = it.boundsInParent.topLeft.x
                                mainViewModel.yOffset.value = it.boundsInParent.topLeft.y
                            }
            )
            Text(item.user.name)
        }
    }
}

@Composable
fun ListItem(
        moveToNextScreen: (item: User, offset: Offset, sizeList: List<Dp>) -> Unit,
        item: User,
        mainViewModel: MainViewModel,
        itemIndex: Int,
) {
    WithConstraints {
        val screenState = remember { mutableStateOf(false) }
        val fixedWidth = 70.dp
        val fixedHeight = 70.dp
        val width = remember { mutableStateOf(fixedWidth) }
        val height = remember { mutableStateOf(fixedHeight) }
        val xOffset = remember { mutableStateOf(0f) }
        val yOffset = remember { mutableStateOf(0f) }
        val offset = remember { mutableStateOf(Offset(0))}

        mainViewModel.height.observeForever{
            height.value = it
        }
        mainViewModel.width.observeForever{
            width.value = it
        }
        mainViewModel.xOffset.observeForever{
            xOffset.value = it
        }
        mainViewModel.yOffset.observeForever{
            yOffset.value = it
        }

        if(screenState.value) {
            width.value = fixedWidth
            height.value = fixedHeight
            xOffset.value = 0f
            yOffset.value = 0f
        }

        onActive(callback = {
            screenState.value = true
        })

        Box(Modifier.fillMaxSize()) {
            Row() {
                if(itemIndex == mainViewModel.index.value) {
                    Image(
                            imageResource(id = item.img),
                            Modifier.onGloballyPositioned {
                                offset.value = it.positionInRoot.div(3f)
                            }
                                    .preferredWidth(animate(target = width.value, animSpec = tween(3000)))
                                    .preferredHeight(animate(target = height.value, animSpec = tween(3000)))
                                    .offset(
                                            x = animate(target = xOffset.value.dp, animSpec = tween(3000)),
                                            y = animate(target = yOffset.value.dp, animSpec = tween(3000))
                                    )
                                    .clickable(onClick = {
                                        moveToNextScreen(item, offset.value, listOf(height.value, width.value))
                                        mainViewModel.xOffset.value = offset.value.x
                                        mainViewModel.yOffset.value = offset.value.y
                                        mainViewModel.width.value = width.value
                                        mainViewModel.height.value = height.value
                                        mainViewModel.index.value = itemIndex
                                    })
                    )
                } else {
                    Image(
                            imageResource(id = item.img),
                            Modifier.onGloballyPositioned {
                                offset.value = it.globalPosition.div(3f)
                            }
                                    .preferredWidth(fixedWidth)
                                    .preferredHeight(fixedHeight)
                                    .clickable(onClick = {
                                        moveToNextScreen(item, offset.value, listOf(height.value, width.value))
                                        mainViewModel.xOffset.value = offset.value.x
                                        mainViewModel.yOffset.value = offset.value.y
                                        mainViewModel.width.value = width.value
                                        mainViewModel.height.value = height.value
                                        mainViewModel.index.value = itemIndex
                                    })
                    )
                }
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