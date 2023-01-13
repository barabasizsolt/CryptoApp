package com.barabasizsolt.feature.shared.catalog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import com.barabasizsolt.feature.R

@Composable
fun CryptoAppButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = { if (!isLoading) { onClick() } },
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        modifier = modifier.fillMaxWidth(),
        enabled = enabled
    ) {
        Text(
            text = text.toUpperCase(locale = Locale.current),
            style = MaterialTheme.typography.button,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(all = dimensionResource(id = R.dimen.content_padding))
        )
    }
}