package com.example.cryptoapp.feature.screen.main.user.catalog

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.cryptoapp.R

@Composable
fun ProfileIcon(
    modifier: Modifier = Modifier,
    photoUrl: Uri?
) {
    Image(
        painter = rememberImagePainter(
            data = photoUrl,
            builder = { placeholder(drawableResId = R.drawable.ic_avatar) }
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size = 120.dp)
            .clip(CircleShape)
    )
}

@Composable
fun ProfileTextItem(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit = {},
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    readOnly: Boolean = true
) {
    return OutlinedTextField(
        value = text,
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null) },
        onValueChange = onTextChange,
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        readOnly = readOnly,
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun UpdateProfileButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = { if (!isLoading) { onClick() } },
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.update_profile),
            style = MaterialTheme.typography.button,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(all = dimensionResource(id = R.dimen.content_padding))
        )
    }
}

@Composable
fun SignOutButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = { if (!isLoading) { onClick() } },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_log_ot),
                contentDescription = null,
                modifier = Modifier
                    .size(size = dimensionResource(id = R.dimen.small_recycler_view_card_logo_size))
            )
            Spacer(modifier = Modifier.width(width = dimensionResource(id = R.dimen.content_padding)))
            Text(
                text = stringResource(id = R.string.sign_out),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold
    )
}