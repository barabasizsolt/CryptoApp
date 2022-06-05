package com.example.cryptoapp.feature.screen.auth.login.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cryptoapp.R

@Composable
fun LoginScreenLogo(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.ic_bitcoin), 
        contentDescription = null,
        tint = MaterialTheme.colors.primary,
        modifier = modifier.size(size = 150.dp)
    )
}

@Composable
fun ForgotPasswordButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(
            text = stringResource(id = R.string.forgot_password),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary
        )
    }
}

@Composable
fun SignUpButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.dont_have_account),
            style = MaterialTheme.typography.body1
        )
        TextButton(onClick = onClick) {
            Text(
                text = stringResource(id = R.string.sign_up),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GoogleSingUpButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    Column(modifier = modifier.fillMaxWidth()) {

        Card(
            onClick = onClick,
            backgroundColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Color.Gray, shape = MaterialTheme.shapes.medium)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = null,
                    modifier = Modifier
                        .size(size = dimensionResource(id = R.dimen.small_recycler_view_card_logo_size))
                )
                Spacer(modifier = Modifier.width(width = dimensionResource(id = R.dimen.content_padding)))
                Text(
                    text = text,
                    style = MaterialTheme.typography.body1,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.Center),
            )
            Box(
                modifier = Modifier
                    .background(color = if (isSystemInDarkTheme()) Color(0xFF121212) else Color.White)
                    .align(alignment = Alignment.Center),
            ) {
                Text(
                    text = "Or",
                    modifier = Modifier.padding(
                        vertical = dimensionResource(id = R.dimen.screen_padding),
                        horizontal = dimensionResource(id = R.dimen.content_padding)
                    )
                )
            }
        }
    }
}