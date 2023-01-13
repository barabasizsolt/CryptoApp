package com.barabasizsolt.feature.screen.main.user.catalog

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.barabasizsolt.feature.R

@Composable
fun ProfileIcon(
    modifier: Modifier = Modifier,
    photoUrl: Uri?,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        Image(
            painter = rememberImagePainter(
                data = photoUrl,
                //data = Uri.decode("content://com.android.providers.media.documents/document/image%3A216"),
                builder = { placeholder(drawableResId = R.drawable.ic_avatar) }
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(size = 120.dp)
                .clip(shape = CircleShape)
                .align(alignment = Alignment.Center)
        )

        CameraIcon(
            modifier = Modifier
                .align(alignment = Alignment.CenterEnd)
                .offset(x = dimensionResource(id = R.dimen.content_padding) * 2)
                .clip(shape = CircleShape)
                .clickable { onClick() }
        )
    }
}

@Composable
private fun CameraIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(shape = CircleShape)
            .background(color = MaterialTheme.colors.primary)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_camera),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(size = 36.dp)
                .clip(shape = CircleShape)
                .align(alignment = Alignment.Center)
                .padding(all = dimensionResource(id = R.dimen.content_padding))
        )
    }
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
fun Header(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    onCancelClicked: () -> Unit,
    onTakePhotoClicked: () -> Unit,
    onOpenGalleryClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        BottomSheetHeader(modifier = Modifier.padding(all = dimensionResource(id = R.dimen.screen_padding)), onClick = onCancelClicked)
        BottomSheetItem(text = "Take a photo", showDivider = true, onClick = onTakePhotoClicked)
        BottomSheetItem(text = "Open gallery", onClick = onOpenGalleryClicked)
    }
}

@Composable
private fun BottomSheetHeader(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Header(text = "Change user avatar")
        Text(
            text = stringResource(id = R.string.cancel),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.clickable { onClick() }
        )
    }
}

@Composable
private fun BottomSheetItem(
    modifier: Modifier = Modifier,
    text: String,
    showDivider: Boolean = false,
    onClick: () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(all = dimensionResource(id = R.dimen.screen_padding))
            )
        }
        if (showDivider) {
            Divider(
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.screen_padding))
            )
        }
    }
}