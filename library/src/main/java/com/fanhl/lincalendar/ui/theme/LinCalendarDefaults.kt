package com.fanhl.lincalendar.ui.theme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale

object LinCalendarDefaults {
    @OptIn(ExperimentalFoundationApi::class)
    fun monthField(
        modifier: Modifier = Modifier,
        firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
        weekHeaderField: @Composable (ColumnScope.() -> Unit) = weekHeaderField(
            firstDayOfWeek = firstDayOfWeek,
        ),
    ): @Composable PagerScope.(
        /** 当前显示日期；用于判断当前显示的月份/周。（之前用YearMonth，但是无法兼容周视图，这里统一改成 LocalDate） */
        localDate: LocalDate,
        /** 当前选中的日期 */
        selectedDate: LocalDate?,
    ) -> Unit = { localDate, selectedDate ->
        val yearMonth = remember { YearMonth.from(localDate) }
        val firstDayOfMonth = remember { yearMonth.atDay(1) }
        val dayOfWeekOfFirstDay = firstDayOfMonth.dayOfWeek.value
        val weeks = remember { ((dayOfWeekOfFirstDay - firstDayOfWeek.value) + yearMonth.lengthOfMonth() + /*向上取整*/6) / 7 }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
        ) {
            weekHeaderField()
            for (it in 0 until weeks) {
                // 当周第一天是当月多少号
                val dayOfMonthAtWeek = -(dayOfWeekOfFirstDay - firstDayOfWeek.value) + it * 7 + 1
                AnimatedVisibility(
                    visible = true,
                ) {
                    Row(
                    ) {
                        for (i in 0 until 7) {
                            val dayOfMonth = dayOfMonthAtWeek + i
                            Box(
                                modifier = Modifier
                                    .height(36.dp)
                                    .weight(1f),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = dayOfMonth.toString(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun weekHeaderField(
        modifier: Modifier = Modifier,
        firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    ): @Composable (ColumnScope.() -> Unit) = {
        val locale = remember { Locale.getDefault() }
        val sortedDaysOfWeek = remember {
            DayOfWeek.entries.run {
                slice(firstDayOfWeek.ordinal until size) + slice(0 until firstDayOfWeek.ordinal)
            }
        }
        Row(
            modifier = Modifier
                .height(32.dp)
                .then(modifier),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            sortedDaysOfWeek.forEach {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = it.getDisplayName(java.time.format.TextStyle.SHORT, locale),
                        style = TextStyle(),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun MonthFieldPreview() {
    HorizontalPager(state = rememberPagerState { 1 }) {
        LinCalendarDefaults.monthField()(
            LocalDate.now(),
            null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WeekHeaderFieldPreview() {
    Column {
        LinCalendarDefaults.weekHeaderField()()
    }
}