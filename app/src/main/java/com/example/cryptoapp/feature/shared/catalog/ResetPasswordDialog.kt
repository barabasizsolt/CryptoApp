package com.example.cryptoapp.feature.shared.catalog

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.cryptoapp.R
import com.google.android.material.composethemeadapter.MdcTheme

@Composable
fun ResetPasswordDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onAccess: () -> Unit,
    email: String,
    onEmailChange: (String) -> Unit
) = MdcTheme {
    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            ResetPasswordCard(
                onAccess = onAccess,
                email = email,
                onEmailChange = onEmailChange
            )
        },
        modifier = modifier
    )
}

@Composable
private fun ResetPasswordCard(
    modifier: Modifier = Modifier,
    onAccess: () -> Unit,
    email: String,
    onEmailChange: (String) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = if (isSystemInDarkTheme())
            dimensionResource(id = R.dimen.dark_mode_card_elevation) else dimensionResource(id = R.dimen.zero_elevation)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.content_padding)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(id = R.dimen.screen_padding))
        ) {
            Text(
                text = stringResource(id = R.string.reset_password_dialog_title),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSurface
            )
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text(text = stringResource(id = R.string.email)) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colors.onSurface,
                    focusedBorderColor = MaterialTheme.colors.primary,
                    unfocusedBorderColor = MaterialTheme.colors.onSurface,
                    focusedLabelColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = MaterialTheme.colors.onSurface,
                    cursorColor = MaterialTheme.colors.onSurface
                ),
                textStyle = MaterialTheme.typography.subtitle1,
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CryptoAppButton(
                    text = stringResource(id = R.string.reset_password),
                    enabled = email.isNotEmpty(),
                    isLoading = false,
                    onClick = onAccess
                )
            }
        }
    }
}
