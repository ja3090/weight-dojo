package com.example.weightdojo.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.weightdojo.components.text.TextDefault
import com.example.weightdojo.datatransferobjects.NutrimentTotalsByDay
import com.example.weightdojo.ui.CustomColors
import com.example.weightdojo.ui.Sizing

@Composable
fun NutrimentInfo(nutrimentTotals: List<NutrimentTotalsByDay>) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = Sizing.paddings.small)

    ) {
        nutrimentTotals.mapIndexed { index, it ->
            NutrimentTotalBar(
                index,
                it,
            )
        }
    }
}

@Composable
fun NutrimentTotalBar(
    index: Int,
    total: NutrimentTotalsByDay,
) {
    val getColour = { index: Int ->
        val colourIndex = index % CustomColors.colorList.size
        CustomColors.colorList[colourIndex]
    }
    val height = 80.dp
    val colour = getColour(index)

    val lag = Sizing.paddings.small.times(5)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp - lag
    val singleWidth = screenWidth / 4


    val percentOfTarget = when (total.dailyIntakeTarget) {
        is Float -> (total.totalGrams / total.dailyIntakeTarget)
        else -> if (total.totalGrams == 0f) 0f else 1f
    }

    val gramsValue = when (total.dailyIntakeTarget) {
        is Float -> "${total.totalGrams.toInt()} / ${total.dailyIntakeTarget}g"
        else -> "${total.totalGrams.toInt()}g"
    }

    Column(
        modifier = Modifier
            .padding(start = if (index == 0) 0.dp else Sizing.paddings.small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = total.nutrimentName,
            modifier = Modifier
                .padding(bottom = Sizing.paddings.small)
                .width(singleWidth)
                .clipToBounds(),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.primary.copy(0.85f)
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colors.background)
                .height(height)
                .width(singleWidth)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                    .fillMaxHeight(percentOfTarget)
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd)
                    .background(colour.copy(0.2f))
            )
        }
        TextDefault(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .fillMaxWidth()
                .padding(top = Sizing.paddings.small),
            text = gramsValue,
            fontSize = Sizing.font.small,
            textAlign = TextAlign.Center
        )
    }
}