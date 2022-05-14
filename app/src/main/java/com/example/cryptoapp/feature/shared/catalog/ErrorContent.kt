package com.example.cryptoapp.feature.shared.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.cryptoapp.R
import com.example.cryptoapp.feature.shared.theme.getBackgroundColor
import com.example.cryptoapp.feature.shared.theme.getContentColor
import com.example.cryptoapp.feature.shared.theme.getPrimaryColor

@Composable
fun ErrorContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(height = dimensionResource(id = R.dimen.chart_height).div(other = 2))
            .padding(all = dimensionResource(id = R.dimen.content_padding)),
        backgroundColor = getBackgroundColor()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.something_went_wrong),
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                color = getContentColor()
            )
            Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.content_padding)))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = dimensionResource(id = R.dimen.button_height))
                    .wrapContentWidth(),
                onClick = onClick,
                content = {
                    Text(text = stringResource(id = R.string.try_again))
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black,
                    backgroundColor = getPrimaryColor()
                )
            )
        }
    }
}