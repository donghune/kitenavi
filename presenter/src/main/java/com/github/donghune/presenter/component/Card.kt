package com.github.donghune.presenter.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DoubleLineCard(
    title: String,
    text: String,
    onClick: () -> Unit = {},
    buttonName: String = "",
    buttonClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    modifier = Modifier
                )
                Text(
                    text = text,
                    modifier = Modifier
                )
            }
            if (buttonName.isNotEmpty()) {
                Button(
                    onClick = buttonClick,
                    modifier = Modifier
                ) {
                    Text(text = buttonName)
                }
            }
        }
    }
}
