package com.github.donghune.kitenavi.view.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

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
        ConstraintLayout(
            modifier = Modifier.padding(10.dp)
        ) {
            val (titleText, contentText, button) = createRefs()

            Text(
                text = title,
                modifier = Modifier.constrainAs(titleText) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )
            Text(
                text = text,
                modifier = Modifier.constrainAs(contentText) {
                    top.linkTo(titleText.bottom)
                    start.linkTo(parent.start)
                }
            )
            if (buttonName.isNotEmpty()) {
                Button(
                    onClick = buttonClick,
                    modifier = Modifier.constrainAs(button) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                ) {
                    Text(text = buttonName)
                }
            }
        }
    }
}
