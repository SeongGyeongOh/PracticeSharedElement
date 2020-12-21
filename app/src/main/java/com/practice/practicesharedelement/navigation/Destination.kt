package com.practice.practicesharedelement.navigation

import android.os.Parcelable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.practice.practicesharedelement.User
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

sealed class Destination : Parcelable {
    @Parcelize
    object FirstScreen : Destination()

    @Parcelize
    data class SecondScreen(val user: @RawValue User, val offset: Offset) : Destination()
}

class Actions(navigator: Navigator<Destination>) {
    val goToSecond: (item: User, offset: Offset) -> Unit = { item, offset ->
        navigator.navigate(Destination.SecondScreen(item, offset))
    }

    val pressOnBack: () -> Unit = {
        navigator.back()
    }
}
