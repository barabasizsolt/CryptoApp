package com.example.cryptoapp.feature.main.home.catalog

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cryptoapp.R
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun CryptoCurrencyCardHolder(
    modifier: Modifier = Modifier,
    title: String = "Top 3 Cryptocurrency"
) {
    val textColor: Color = if (isSystemInDarkTheme()) colorResource(id = R.color.dark_mode_text_color) else Color.Black

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
        ) {
            repeat(times = 3, action = {
                item {  CryptoCurrencyCard(textColor = textColor) }
            })
        }
    }
}

@Composable
fun CryptoCurrencyCard(
    modifier: Modifier = Modifier,
    name: String = "DefiBox",
    symbol: String = "BOX",
    price: String = "$3,124.12",
    iconUrl: String = "https://cdn.coinranking.com/H2KmEnlag/6960.png",
    textColor: Color
) {
   Card(
       modifier = modifier
           .height(height = 230.dp)
           .width(width = 150.dp),
       elevation = 4.dp,
       backgroundColor = if (isSystemInDarkTheme()) colorResource(id = R.color.dark_mode_background_color) else Color.White
   ) {

       Column(modifier = Modifier.wrapContentSize()) {
           Column(
               modifier = Modifier
                   .padding(all = 12.dp)
                   .weight(weight = 6f),
               verticalArrangement = Arrangement.spacedBy(space = 4.dp)
           ) {
               GlideImage(
                   imageModel = iconUrl,
                   modifier = Modifier
                       .size(size = 50.dp)
                       .clip(shape = CircleShape)
               )
               Text(
                   text = symbol,
                   style = MaterialTheme.typography.h6,
                   fontWeight = FontWeight.Bold,
                   modifier = Modifier.padding(top = 16.dp),
                   color = textColor
               )
               Text(
                   text = name,
                   style = MaterialTheme.typography.caption,
                   fontWeight = FontWeight.Bold,
                   color = textColor
               )
               Text(
                   text = price,
                   style = MaterialTheme.typography.h6,
                   fontWeight = FontWeight.Bold,
                   color = textColor
               )
               PriceChange(change = 0.08, iconColor = textColor)
           }
           Column(
               modifier = Modifier
                   .weight(weight = 1f)
                   .background(color = colorResource(id = R.color.orange))
                   .fillMaxSize(),
               horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.Center
           ) {
                Text(
                    text = "TOP",
                    modifier = Modifier.padding(all = 4.dp)
                )
           }
       }
   }
}

@Composable
private fun PriceChange(
    modifier: Modifier = Modifier,
    change: Double,
    iconColor: Color = Color.Black
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space = 4.dp)
    ) {
        Icon(
            imageVector = when {
                change < 0 -> Icons.Filled.KeyboardArrowDown
                else -> Icons.Filled.KeyboardArrowUp
            },
            contentDescription = null,
            tint = iconColor
        )
        Text(
            text = "$change%",
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Bold,
            color = when {
                change < 0 -> Color.Red
                else -> Color.Green
            },
        )
    }
}

