package com.example.cryptoapp.feature.main.exchange.exchangeDetail.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberImagePainter
import com.example.cryptoapp.R
import com.example.cryptoapp.feature.main.cryptocurrency.cryptocurrencyDetails.helpers.CryptoXAxisFormatter
import com.example.cryptoapp.feature.main.cryptocurrency.cryptocurrencyDetails.helpers.UnitOfTimeType
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.color.MaterialColors

@Composable
private fun ExchangeDetailDivider() {
    Divider(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Gray,
        thickness = 0.7.dp
    )
}

@Composable
fun ExchangeDetailHeader(
    modifier: Modifier = Modifier,
    url: String,
    title: String,
    contentColor: Color
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
                .offset(x = 25.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(data = url),
                contentDescription = null,
                modifier = Modifier
                    .size(size = 50.dp)
                    .clip(shape = CircleShape)
                    .border(
                        width = 1.dp,
                        color = contentColor,
                        shape = CircleShape
                    ),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(width = 4.dp))
            Box(modifier = Modifier.height(height = 50.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    color = contentColor,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(alignment = Alignment.TopStart)
                )
                Text(
                    text = "Exchange",
                    style = MaterialTheme.typography.body2,
                    color = contentColor,
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
    lineDataSet: LineDataSet,
    unitOfTimeType: UnitOfTimeType = UnitOfTimeType.UNIT_24H
) {
    AndroidView(
        factory = { context ->
            LineChart(context)
        },
        update = { lineChart ->
            lineDataSet.apply {
                color = MaterialColors.getColor(lineChart.context, R.attr.app_text_color, android.graphics.Color.WHITE)
                highLightColor = MaterialColors.getColor(lineChart.context, R.attr.crypto_chart_color, android.graphics.Color.WHITE)
                fillColor = MaterialColors.getColor(lineChart.context, R.attr.crypto_chart_color, android.graphics.Color.WHITE)
            }
            lineChart.apply {
                extraBottomOffset = 5f
                setTouchEnabled(false)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(false)
                setDrawGridBackground(false)
                description.isEnabled = false
                legend.isEnabled = true
                legend.textColor = MaterialColors.getColor(context, R.attr.app_text_color, android.graphics.Color.WHITE)
                legend.textSize = 13f
                legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                legend.orientation = Legend.LegendOrientation.HORIZONTAL
                legend.setDrawInside(false)
                legend.setCustom(
                    arrayListOf(
                        LegendEntry().also {
                            it.label = resources.getString(R.string.exchange_volume_changes)
                            it.formColor = MaterialColors.getColor(context, R.attr.crypto_chart_color, android.graphics.Color.WHITE)
                        }
                    )
                )
                xAxis.textColor = MaterialColors.getColor(context, R.attr.app_text_color, android.graphics.Color.WHITE)
                xAxis.textSize = 12f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(true)
                xAxis.isGranularityEnabled = true
                axisLeft.textColor = MaterialColors.getColor(context, R.attr.app_text_color, android.graphics.Color.WHITE)
                axisLeft.valueFormatter = ExchangeYAxisFormatter()
                axisLeft.setDrawGridLines(true)
                axisRight.isEnabled = false
                setBackgroundColor(MaterialColors.getColor(context, R.attr.app_background_color, android.graphics.Color.WHITE))
                xAxis.valueFormatter = CryptoXAxisFormatter(unitOfTimeType = unitOfTimeType)
                data = LineData(arrayListOf<ILineDataSet>(lineDataSet))
                notifyDataSetChanged()
                invalidate()
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(height = 300.dp)
    )
    ExchangeDetailDivider()
}

@Composable
fun ExchangeDetailCardHolder(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    contentColor: Color,
    items: List<String>,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = horizontalArrangement
        ) {
            repeat(times = items.size, action = { index ->
                ExchangeDetailCard(
                    modifier = Modifier.padding(all = 4.dp),
                    text = items[index],
                    backgroundColor = backgroundColor,
                    contentColor = contentColor
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
    backgroundColor: Color,
    contentColor: Color,
    items: List<String>,
    onClick: (Int) -> Unit
) {
    var isSelected by remember { mutableStateOf(0) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(times = items.size, action = { index ->
                ExchangeDetailChip(
                    modifier = Modifier.padding(all = 4.dp),
                    text = items[index],
                    backgroundColor = backgroundColor,
                    contentColor = contentColor,
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
                .padding(all = 8.dp),
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
    time: String,
    tickers: String,
    contentColor: Color
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
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
                    color = contentColor,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(height = 4.dp))
                Text(
                    text = "VOL/BTC/24H - AVG - $time",
                    style = MaterialTheme.typography.caption,
                    color = contentColor,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = "$tickers available tickers",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                color = contentColor
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
    contentColor: Color,
    onClick: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
                .clickable { onClick() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            Text(
                text = text,
                style = MaterialTheme.typography.caption,
                color = contentColor,
                overflow = TextOverflow.Ellipsis
            )
        }
        ExchangeDetailDivider()
    }
}
