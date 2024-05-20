package com.fanhl.lincalendar.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate

object LinCalendarDefaults {
    @OptIn(ExperimentalFoundationApi::class)
    fun monthField(
        modifier: Modifier = Modifier,
        weekHeaderField: @Composable ColumnScope.() -> Unit = weekHeaderField()
    ): @Composable PagerScope.(selectedDate: LocalDate?) -> Unit = { selectedDate ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier),
        ) {
            weekHeaderField()
            Text(
                text = "Calendar WeekField",
                modifier = Modifier.weight(5f),
            )
        }
    }

    fun weekHeaderField(
        modifier: Modifier = Modifier,
    ): @Composable (ColumnScope.() -> Unit) = {
        // WeekHeaderField
        Row(
            modifier = Modifier
                .height(36.dp)
                .then(modifier),
        ) {
            Text(text = "Calendar")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, widthDp = 300, heightDp = 240)
@Composable
private fun MonthFieldPreview() {
    HorizontalPager(state = rememberPagerState { 1 }) {
        LinCalendarDefaults.monthField()(LocalDate.now())
    }
}