package com.practice.practicesharedelement.navigation

import android.os.Parcelable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.practice.practicesharedelement.User
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

sealed class Destination : Parcelable {
    @Parcelize
    object FirstScreen : Destination()

    @Parcelize
    data class SecondScreen(val user: @RawValue User,
                            val offset: Offset,
                            val list: @RawValue List<Dp>) : Destination()
}

class Actions(navigator: Navigator<Destination>) {
    val goToSecond: (item: User, offset: Offset, list: List<Dp>) -> Unit = { item, offset, list ->
        navigator.navigate(Destination.SecondScreen(item, offset, list))
    }

    val pressOnBack: () -> Unit = {
        navigator.back()
    }
}
