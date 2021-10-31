package com.github.donghune.kitenavi.view.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.github.donghune.kitenavi.R

@Composable
fun AddImageCard() {
    ImageCard(id = R.drawable.ic_baseline_add_circle_24)
}

@Composable
fun ImageCard(@DrawableRes id: Int) {
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = id),
            "add button",
            modifier = Modifier.padding(10.dp)
        )
    }
}
