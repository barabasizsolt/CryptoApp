package com.example.cryptoapp.wear.screen.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.CurvedTextStyle
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.curvedText

@ExperimentalWearMaterialApi
@Composable
fun Header(
    text: String,
    modifier: Modifier = Modifier,
    timeTextStyle: TextStyle = TimeTextDefaults.timeTextStyle(),
    contentPadding: PaddingValues = PaddingValues(all = 4.dp)
) {
    if (LocalConfiguration.current.isScreenRound) {
        CurvedLayout(modifier.padding(contentPadding)) {
            curvedText(
                text = text,
                style = CurvedTextStyle(timeTextStyle)
            )
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues = contentPadding),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                maxLines = 1,
                style = timeTextStyle,
            )
        }
    }
}