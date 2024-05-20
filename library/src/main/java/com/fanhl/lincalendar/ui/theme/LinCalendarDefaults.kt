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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

object LinCalendarDefaults {
    @OptIn(ExperimentalFoundationApi::class)
    fun monthField(
        modifier: Modifier = Modifier,
        firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
        weekHeaderField: @Composable (ColumnScope.() -> Unit) = weekHeaderField(
            firstDayOfWeek = firstDayOfWeek,
        ),
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
        firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    ): @Composable (ColumnScope.() -> Unit) = {
        val locale = remember { Locale.getDefault() }
        Row(
            modifier = Modifier
                .height(32.dp)
                .then(modifier),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DayOfWeek.entries.forEach {
                Text(
                    text = it.getDisplayName(TextStyle.SHORT, locale),
                    style = androidx.compose.ui.text.TextStyle(
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, widthDp = 320, heightDp = 240)
@Composable
private fun MonthFieldPreview() {
    HorizontalPager(state = rememberPagerState { 1 }) {
        LinCalendarDefaults.monthField()(LocalDate.now())
    }
}

@Preview(showBackground = true)
@Composable
private fun WeekHeaderFieldPreview() {
    Column {
        LinCalendarDefaults.weekHeaderField()()
    }
}