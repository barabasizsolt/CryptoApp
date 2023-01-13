package com.example.cryptoapp.wear.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.CurvedTextStyle
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.curvedText
import com.example.cryptoapp.wear.R

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

@Composable
fun WearButton(
    painterRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(color = 0xFFFF9800),
            contentColor = Color.Black
        ),
        modifier = modifier,
        enabled = true
    ) {
        Icon(
            painter = painterResource(id = painterRes),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .size(size = ButtonDefaults.DefaultIconSize)
                .wrapContentSize(align = Alignment.Center)
        )
    }
}