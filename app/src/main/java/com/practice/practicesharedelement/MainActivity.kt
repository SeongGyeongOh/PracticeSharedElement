package com.practice.practicesharedelement

import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animate
import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.practice.practicesharedelement.navigation.Actions
import com.practice.practicesharedelement.navigation.AmbientBackDispatcher
import com.practice.practicesharedelement.navigation.Destination
import com.practice.practicesharedelement.navigation.Navigator
import com.practice.practicesharedelement.ui.PracticeSharedElementTheme

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setContent {
            PracticeSharedElementTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    RootScreen(
                            backDispatcher = onBackPressedDispatcher,
                            viewModel
                    )
                }
            }
        }
    }
}


@Composable
fun RootScreen(backDispatcher: OnBackPressedDispatcher, viewModel: MainViewModel) {
    val navigator: Navigator<Destination> = rememberSavedInstanceState(
        saver = Navigator.saver(backDispatcher)
    ) {
        Navigator(Destination.FirstScreen, backDispatcher)
    }
    val actions = remember(navigator) { Actions(navigator) }
    val screenState = remember { mutableStateOf(false)}
    Box(Modifier.fillMaxSize()) {
        Providers(AmbientBackDispatcher provides backDispatcher) {
            Crossfade(current = navigator.current) { destination ->
                when(destination) {
                    is Destination.FirstScreen -> {
                        Screen1(
                            moveToNextScreen = actions.goToSecond,
                                viewModel
                        )
                    }
                    is Destination.SecondScreen -> {
                        Screen2(destination, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Screen1(
        moveToNextScreen: (item: User, offset: Offset) -> Unit,
        viewModel: MainViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        WithConstraints() {
            LazyColumn(Modifier.fillMaxWidth()) {
                items(tempList) { item ->
                    ListItem(
                            moveToNextScreen = moveToNextScreen,
                            item = item,
                            viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Screen2(
        item: Destination.SecondScreen,
        viewModel: MainViewModel,
) {
    val xOffset = remember { mutableStateOf(item.offset.x)}
    val yOffset = remember { mutableStateOf(item.offset.y) }
    viewModel.screenState.observeForever {
        if(!it) {
            xOffset.value = 100f
            yOffset.value = 0f
        }else {
            xOffset.value = item.offset.x
            yOffset.value = item.offset.y
        }
    }

    WithConstraints {
        Column(
            modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray),

        ) {
            Image(
                    imageResource(id = item.user.img),
                    Modifier.preferredSize(50.dp)
                            .offset(
                                x = animate(target = xOffset.value.dp),
                                y = animate(target = yOffset.value.dp)
                            )
            )
            Text(item.user.name)
        }
    }

}

@Composable
fun ListItem(
        moveToNextScreen: (item: User, offset: Offset) -> Unit,
        item: User,
        viewModel: MainViewModel,
) {
    WithConstraints {
        val expand = remember { mutableStateOf(false) }
        val height = if (expand.value) 1000.dp else 50.dp

        val x = remember { mutableStateOf(0.dp)}
        val y = remember { mutableStateOf(0.dp)}
        val offset = remember { mutableStateOf(Offset(0))}
        val modifier = remember {mutableStateOf(Modifier)}

        Box(Modifier.fillMaxSize().animateContentSize()) {
            Row(Modifier.fillMaxWidth()
                    .height(height = height)
                    .clickable(onClick = {
                        moveToNextScreen(item, offset.value)
                        viewModel._screenState.value = false
                    })
//                    .clickable(onClick = { moveToNextScreen(item, x.value.value.toInt(), y.value.value.toInt()) })
//                    .clickable(onClick = {Log.d("asdf", "${offset.value}")})
            )
            {
                Image(
                    imageResource(id = item.img),
                    modifier.value.preferredSize(50.dp)
                            .onGloballyPositioned {
                                offset.value = it.positionInRoot.div(3f)
//                                y.value = it.globalPosition.y.dp
                            }
//                        .offset(
////                            x = androidx.compose.animation.animate(target = imageX.value.dp),
////                            y = androidx.compose.animation.animate(target = imageY.value.dp)
//                            x = 100.dp,
//                            y = 0.dp
//                        )
                )
                Text(item.name)
            }

        }
    }
}

@Composable
fun ItemScreen(item: User, listState: MutableState<Boolean>) {
    WithConstraints() {
        val width = with(AmbientDensity.current) { constraints.maxWidth.toDp()}
        val height = with(AmbientDensity.current) { constraints.maxWidth.toDp()}
        Column(
            Modifier.width(width)
                .height(height)
                .background(Color.Gray)
                .animateContentSize()
                .clickable(onClick = {listState.value = !listState.value})
        ) {
            Image(imageResource(id = item.img))
            Text(item.name)
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