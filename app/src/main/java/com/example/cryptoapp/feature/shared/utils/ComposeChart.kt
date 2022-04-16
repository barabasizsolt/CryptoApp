package com.example.cryptoapp.feature.shared.utils

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class LineChartDataValue(
    val x: Long,
    val y: Double
)

data class LineChartData(
    val maxValue: Double,
    val values: List<LineChartDataValue>
)

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    lineColor: Color = Color.White,
    strokeWidth: Float = 8f,
    data: LineChartData
) {
    if (data.values.isEmpty()) return

    Canvas(modifier = modifier) {
        val totalRecords = data.values.size
        val lineDistance = size.width / (totalRecords + 1)
        val cHeight = size.height
        var currentLineDistance = 0F + lineDistance

        data.values.forEachIndexed { index, transactionRate ->
            if (totalRecords >= index + 2) {
                drawLine(
                    start = Offset(
                        x = currentLineDistance,
                        y = calculateYCoordinate(
                            higherTransactionRateValue = data.maxValue,
                            currentTransactionRate = transactionRate.y,
                            canvasHeight = cHeight
                        )
                    ),
                    end = Offset(
                        x = currentLineDistance + lineDistance,
                        y = calculateYCoordinate(
                            higherTransactionRateValue = data.maxValue,
                            currentTransactionRate = data.values[index + 1].y,
                            canvasHeight = cHeight
                        )
                    ),
                    color = lineColor,
                    strokeWidth = strokeWidth
                )
            }
            currentLineDistance += lineDistance
        }
    }
}

private fun calculateYCoordinate(
    higherTransactionRateValue: Double,
    currentTransactionRate: Double,
    canvasHeight: Float
): Float {
    val maxAndCurrentValueDifference = (higherTransactionRateValue - currentTransactionRate)
        .toFloat()
    val relativePercentageOfScreen = (canvasHeight / higherTransactionRateValue)
        .toFloat()
    return maxAndCurrentValueDifference * relativePercentageOfScreen
}
