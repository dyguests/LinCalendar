package com.fanhl.lincalendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale

object LinCalendarDefaults {
    fun option(
        headerHeight: Dp = 32.dp,
        rowHeight: Dp = 36.dp,
        firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
        weekDisplayMode: LinCalendar.WeekDisplayMode = LinCalendar.WeekDisplayMode.FIXED_HEIGHT,
        locale: Locale = Locale.getDefault(),
    ) = LinCalendar.Option(
        headerHeight = headerHeight,
        rowHeight = rowHeight,
        firstDayOfWeek = firstDayOfWeek,
        weekDisplayMode = weekDisplayMode,
        locale = locale
    )

    @OptIn(ExperimentalFoundationApi::class)
    fun monthField(
        modifier: Modifier = Modifier,
        options: LinCalendar.Option = option(),
        weekHeaderField: @Composable (ColumnScope.() -> Unit) = weekHeaderField(),
        weekField: @Composable AnimatedVisibilityScope.(yearMonth: YearMonth, firstDateOfWeek: LocalDate) -> Unit = weekField(options),
    ): @Composable PagerScope.(
        /** 当前显示日期；用于判断当前显示的月份/周。（之前用YearMonth，但是无法兼容周视图，这里统一改成 LocalDate） */
        period: LocalDate,
        /** 当前选中的日期 */
        selectedDate: LocalDate?,
    ) -> Unit = { period, selectedDate ->
        val yearMonth = remember { YearMonth.from(period) }
        val firstDayOfMonth = remember { yearMonth.atDay(1) }
        val weeks = remember {
            val dayOfWeekOfFirstDay = firstDayOfMonth.dayOfWeek.value
            ((dayOfWeekOfFirstDay - options.firstDayOfWeek.value) + yearMonth.lengthOfMonth() + /*向上取整*/6) / 7
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
        ) {
            weekHeaderField()
            for (week in 1..5) {
                // 当周第一天是当月多少号
                val firstDayOfMonthAtWeek = (week - 1) * 7 - (firstDayOfMonth.dayOfWeek.value - options.firstDayOfWeek.value)
                val firstDateOfWeek = firstDayOfMonth.plusDays(firstDayOfMonthAtWeek.toLong())

                AnimatedVisibility(
                    visible = true,
                ) {
                    if (week <= weeks) {
                        weekField(yearMonth, firstDateOfWeek)
                    } else {
                        Box(
                            modifier = Modifier
                                .height(options.rowHeight)
                                .weight(1f),
                        ) { }
                    }
                }
            }
        }
    }


    fun weekHeaderField(
        modifier: Modifier = Modifier,
        options: LinCalendar.Option = option(),
        dayHeaderField: @Composable() (RowScope.(dayOfWeek: DayOfWeek) -> Unit) = dayHeaderField(),
    ): @Composable (ColumnScope.() -> Unit) = {
        val locale = remember { Locale.getDefault() }
        val sortedDaysOfWeek = remember {
            DayOfWeek.entries.run {
                slice(options.firstDayOfWeek.ordinal until size) + slice(0 until options.firstDayOfWeek.ordinal)
            }
        }
        Row(
            modifier = Modifier
                .height(options.headerHeight)
                .then(modifier),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            sortedDaysOfWeek.forEach {
                dayHeaderField(it)
            }
        }
    }

    fun dayHeaderField(
    ): @Composable RowScope.(dayOfWeek: DayOfWeek) -> Unit = { dayOfWeek ->
        val locale = remember { Locale.getDefault() }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, locale),
                style = TextStyle(),
            )
        }
    }

    fun weekField(
        options: LinCalendar.Option,
        dayField: @Composable RowScope.(yearMonth: YearMonth, localDate: LocalDate) -> Unit = dayField(options)
    ): @Composable AnimatedVisibilityScope.(
        yearMonth: YearMonth,
        /** 当前周的第一天 */
        firstDateOfWeek: LocalDate,
    ) -> Unit = { yearMonth, firstDateOfWeek ->
        Row {
            for (i in 0 until 7) {
                val localDate = firstDateOfWeek.plusDays(i.toLong())
                dayField(yearMonth, localDate)
            }
        }
    }

    fun dayField(
        options: LinCalendar.Option,
    ): @Composable RowScope.(
        yearMonth: YearMonth,
        localDate: LocalDate,
    ) -> Unit = { yearMonth, localDate ->
        Box(
            modifier = Modifier
                .height(options.rowHeight)
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = localDate.dayOfMonth.toString(),
                style = TextStyle(
                    color = if (yearMonth.month == localDate.month) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.secondary,
                ),
            )
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

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun MonthFieldPreview2() {
    HorizontalPager(state = rememberPagerState { 1 }) {
        LinCalendarDefaults.monthField()(
            LocalDate.of(2021, 2, 1),
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