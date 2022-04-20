package com.example.cryptoapp.feature.screen.main.exchange.exchangeDetail.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberImagePainter
import com.example.cryptoapp.R
import com.example.cryptoapp.feature.screen.main.cryptocurrency.cryptocurrencyDetails.helpers.UnitOfTimeType
import com.example.cryptoapp.feature.screen.main.exchange.exchangeDetail.ExchangeDetailUiModel
import com.example.cryptoapp.feature.shared.theme.getBackgroundColor
import com.example.cryptoapp.feature.shared.theme.getContentColor
import com.example.cryptoapp.feature.shared.utils.getFormattedHour
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.color.MaterialColors

@Composable
fun ExchangeDetailHeader(
    modifier: Modifier = Modifier,
    url: String,
    title: String
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(id = R.dimen.content_padding))
                .offset(x = dimensionResource(id = R.dimen.exchange_header_offset)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(data = url),
                contentDescription = null,
                modifier = Modifier
                    .size(size = dimensionResource(id = R.dimen.exchange_header_img_size))
                    .clip(shape = CircleShape)
                    .border(
                        width = 1.dp,
                        color = getContentColor(),
                        shape = CircleShape
                    ),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(width = dimensionResource(id = R.dimen.content_padding_half)))
            Box(modifier = Modifier.height(height = dimensionResource(id = R.dimen.exchange_header_img_size))) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    color = getContentColor(),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(alignment = Alignment.TopStart)
                )
                Text(
                    text = stringResource(id = R.string.exchange),
                    style = MaterialTheme.typography.body2,
                    color = getContentColor(),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(alignment = Alignment.BottomStart)
                )
            }
        }
        ExchangeDetailDivider()
    }
}

@Composable
fun ExchangeDetailChart(
    modifier: Modifier = Modifier,
    history: ExchangeDetailUiModel.ExchangeDetailHistory?,
    unitOfTimeType: UnitOfTimeType
) {
    if (history == null) {
        ExchangeDetailChartPlaceHolder(modifier = modifier)
    } else {
        ExchangeDetailLineChart(
            modifier = modifier,
            history = history,
            unitOfTimeType = unitOfTimeType
        )
    }
    ExchangeDetailDivider()
}

@Composable
private fun ExchangeDetailLineChart(
    modifier: Modifier = Modifier,
    history: ExchangeDetailUiModel.ExchangeDetailHistory,
    unitOfTimeType: UnitOfTimeType
) {
    val textColor = MaterialColors.getColor(LocalContext.current, R.attr.app_text_color, android.graphics.Color.WHITE)
    val backgroundColor = MaterialColors.getColor(LocalContext.current, R.attr.app_background_color, android.graphics.Color.WHITE)
    val chartColor = MaterialColors.getColor(LocalContext.current, R.attr.crypto_chart_color, android.graphics.Color.WHITE)

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                extraBottomOffset = 5f
                setTouchEnabled(false)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(false)
                setDrawGridBackground(false)
                description.isEnabled = false
                legend.isEnabled = true
                legend.textColor = textColor
                legend.textSize = 13f
                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                legend.orientation = Legend.LegendOrientation.HORIZONTAL
                legend.setDrawInside(false)
                legend.setCustom(
                    arrayListOf(
                        LegendEntry().also {
                            it.label = resources.getString(R.string.exchange_volume_changes)
                            it.formColor = chartColor
                        }
                    )
                )
                xAxis.textColor = textColor
                xAxis.textSize = 12f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(true)
                xAxis.isGranularityEnabled = true
                axisLeft.textColor = textColor
                axisLeft.valueFormatter = ExchangeYAxisFormatter()
                axisLeft.setDrawGridLines(true)
                axisRight.isEnabled = false
                setBackgroundColor(backgroundColor)
            }
        },
        update = { lineChart ->
            history.dataSet.apply {
                color = textColor
                highLightColor = chartColor
                fillColor = chartColor
            }
            lineChart.apply {
                xAxis.valueFormatter = ExchangeXAxisFormatter(unitOfTimeType = unitOfTimeType)
                data = LineData(arrayListOf<ILineDataSet>(history.dataSet))
                notifyDataSetChanged()
                invalidate()
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(height = dimensionResource(id = R.dimen.chart_height))
    )
}

@Composable
private fun ExchangeDetailChartPlaceHolder(modifier: Modifier = Modifier){
    Card(
        modifier = modifier
            .padding(all = dimensionResource(id = R.dimen.content_padding))
            .height(height = dimensionResource(id = R.dimen.chart_height)),
        backgroundColor = getBackgroundColor()
    ) {
        Column {
            Box(modifier = Modifier.fillMaxSize()){
                CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.Center), color = getContentColor())
            }
            ExchangeDetailDivider()
        }
    }
}

@Composable
fun ExchangeDetailCardHolder(
    modifier: Modifier = Modifier,
    items: List<String>,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(id = R.dimen.content_padding_half)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = horizontalArrangement
        ) {
            repeat(times = items.size, action = { index ->
                ExchangeDetailCard(
                    modifier = Modifier.padding(all = dimensionResource(id = R.dimen.content_padding_half)),
                    text = items[index],
                    backgroundColor = getBackgroundColor(),
                    contentColor = getContentColor()
                )
            })
        }
        ExchangeDetailDivider()
    }
}

@Composable
private fun ExchangeDetailCard(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color,
    contentColor: Color
) {
    CardHolder(
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ExchangeDetailChipGroup(
    modifier: Modifier = Modifier,
    items: List<String> = listOf(stringResource(id = R.string.chip_24hr), stringResource(id = R.string.chip_1w)),
    onClick: (Int) -> Unit
) {
    var isSelected by remember { mutableStateOf(0) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(id = R.dimen.content_padding_half)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(times = items.size, action = { index ->
                ExchangeDetailChip(
                    modifier = Modifier.padding(all = dimensionResource(id = R.dimen.content_padding_half)),
                    text = items[index],
                    backgroundColor = getBackgroundColor(),
                    contentColor = getContentColor(),
                    isSelected = index == isSelected,
                    onClick = {
                        if (isSelected != index) {
                            isSelected = index
                            onClick(index)
                        }
                    }
                )
            })
        }
        ExchangeDetailDivider()
    }
}

@Composable
private fun ExchangeDetailChip(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    CardHolder(
        modifier = modifier,
        backgroundColor = if (isSelected) colorResource(id = R.color.orange) else backgroundColor,
        contentColor = if (isSelected) Color.Black else contentColor,
        onClick = onClick
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CardHolder(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        elevation = 0.dp,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        modifier = modifier.wrapContentSize(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(all = dimensionResource(id = R.dimen.content_padding)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
    }
}

@Composable
fun ExchangeDetailBody(
    modifier: Modifier = Modifier,
    tradeVolume: String,
    tickers: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.content_padding))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.wrapContentWidth()) {
                Text(
                    text = tradeVolume,
                    style = MaterialTheme.typography.h5,
                    color = getContentColor(),
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(height = dimensionResource(id = R.dimen.content_padding_half)))
                Text(
                    text = "VOL/BTC/24H - AVG - ${System.currentTimeMillis().getFormattedHour()}",
                    style = MaterialTheme.typography.caption,
                    color = getContentColor(),
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = "$tickers available tickers",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                color = getContentColor()
            )
        }
    }
    ExchangeDetailDivider()
}

@Composable
fun ExchangeDetailItem(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    onClick: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimensionResource(id = R.dimen.content_padding))
                .clickable { onClick() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Bold,
                color = getContentColor()
            )
            Text(
                text = text,
                style = MaterialTheme.typography.caption,
                color = getContentColor(),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(weight = 1f),
                textAlign = TextAlign.End,
                maxLines = 1
            )
        }
        ExchangeDetailDivider()
    }
}

@Composable
private fun ExchangeDetailDivider() {
    Divider(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Gray,
        thickness = 0.7.dp
    )
}