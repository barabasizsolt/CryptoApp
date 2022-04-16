package com.example.cryptoapp.feature.shared.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import com.example.cryptoapp.R

@Composable
fun ResetPasswordDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onAccess: () -> Unit,
    isDismissed: Boolean,
    email: String,
    onEmailChange: (String) -> Unit
) {
    if(!isDismissed) {
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
}

@Composable
private fun ResetPasswordCard(
    modifier: Modifier = Modifier,
    onAccess: () -> Unit,
    email: String,
    onEmailChange: (String) -> Unit
) {
    val contentColor: Color = if (isSystemInDarkTheme()) Color.Gray else Color.Black

    Card(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = if (isSystemInDarkTheme()) Color(color = 0xFF121212) else Color.White,
        elevation = if (isSystemInDarkTheme()) 24.dp else 0.dp
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.reset_password_dialog_title),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text(text = "Email") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = contentColor,
                    focusedBorderColor = colorResource(id = R.color.orange),
                    unfocusedBorderColor = contentColor,
                    focusedLabelColor = colorResource(id = R.color.orange),
                    unfocusedLabelColor = contentColor,
                    cursorColor = contentColor
                ),
                textStyle = MaterialTheme.typography.subtitle1,
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 40.dp),
                    onClick = onAccess,
                    content = {
                        Text(text = stringResource(id = R.string.reset_password).toUpperCase(locale = Locale.current))
                    },
                    enabled = email.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        backgroundColor = colorResource(id = R.color.orange),
                        disabledBackgroundColor = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray,
                        disabledContentColor = contentColor
                    )
                )
            }
        }
    }
}