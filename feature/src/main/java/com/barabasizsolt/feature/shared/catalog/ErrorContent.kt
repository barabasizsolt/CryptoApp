package com.barabasizsolt.feature.shared.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.barabasizsolt.feature.R
import com.google.android.material.composethemeadapter.MdcTheme

@Composable
fun ErrorContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) = MdcTheme {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(height = dimensionResource(id = R.dimen.chart_height).div(other = 2))
            .padding(all = dimensionResource(id = R.dimen.content_padding)),
        backgroundColor = MaterialTheme.colors.surface
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
                color = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.content_padding)))
            CryptoAppButton(
                text = stringResource(id = R.string.try_again),
                enabled = true,
                isLoading = false,
                onClick = onClick
            )
        }
    }
}