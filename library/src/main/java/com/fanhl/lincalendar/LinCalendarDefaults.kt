package com.fanhl.lincalendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale
import kotlin.math.min

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
    fun monthsField(
        state: LinCalendarState,
        modifier: Modifier = Modifier,
        monthFiled: @Composable() (LazyItemScope.(date: LocalDate) -> Unit) = monthField(
            state = state,
        ),
    ) = @Composable {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .then(other = modifier),
            state = state.listState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = state.listState),
        ) {
            items(
                count = state.pageCount,
                key = { page -> state.getKey(page) },
            ) { page ->
                monthFiled(
                    date = state.getDateByPage(page),
                )
            }
        }
    }

    fun monthField(
        state: LinCalendarState,
        modifier: Modifier = Modifier,
        weekHeaderField: @Composable (ColumnScope.() -> Unit) = weekHeaderField(
            state = state,
        ),
        weekField: @Composable AnimatedVisibilityScope.(yearMonth: YearMonth, firstDateOfWeek: LocalDate) -> Unit = weekField(
            state = state,
        ),
    ): @Composable LazyItemScope.(
        /** 当前显示日期；用于判断当前显示的月份/周。（之前用YearMonth，但是无法兼容周视图，这里统一改成 LocalDate） */
        date: LocalDate,
    ) -> Unit = { date ->
        val yearMonth = remember(date) { YearMonth.from(date) }
        val firstDayOfMonth = remember(date) { yearMonth.atDay(1) }
        val weeks = remember(date) {
            val dayOfWeekOfFirstDay = firstDayOfMonth.dayOfWeek.value
            ((dayOfWeekOfFirstDay - state.option.firstDayOfWeek.value) + yearMonth.lengthOfMonth() + /*向上取整*/6) / 7
        }

        val weekFields = remember {
            WeekFields.of(state.option.firstDayOfWeek, 1)
        }
        // date 所在周是当月的第几周
        val weekOfMonth = remember(date) {
            date.get(weekFields.weekOfMonth())
        }

        Column(
            modifier = Modifier
                .fillParentMaxWidth()
                .then(modifier),
        ) {
            weekHeaderField()
            for (week in 1..5) {
                // 当周第一天是当月多少号
                val firstDayOfMonthAtWeek = (week - 1) * 7 - (firstDayOfMonth.dayOfWeek.value - state.option.firstDayOfWeek.value)
                val firstDateOfWeek = firstDayOfMonth.plusDays(firstDayOfMonthAtWeek.toLong())

                AnimatedVisibility(
                    visible = state.displayMode == LinCalendar.DisplayMode.MONTHLY || weekOfMonth == week,
                ) {
                    if (week <= weeks) {
                        weekField(yearMonth, firstDateOfWeek)
                    } else {
                        Box(
                            modifier = Modifier
                                .height(state.option.rowHeight)
                                .weight(1f),
                        ) { }
                    }
                }
            }
        }
    }


    fun weekHeaderField(
        state: LinCalendarState,
        modifier: Modifier = Modifier,
        dayHeaderField: @Composable (RowScope.(dayOfWeek: DayOfWeek) -> Unit) = dayHeaderField(
            state = state,
        ),
    ): @Composable (ColumnScope.() -> Unit) = {
        val sortedDaysOfWeek = remember {
            DayOfWeek.entries.run {
                slice(state.option.firstDayOfWeek.ordinal until size) + slice(0 until state.option.firstDayOfWeek.ordinal)
            }
        }
        Row(
            modifier = Modifier
                .height(state.option.headerHeight)
                .then(modifier),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            sortedDaysOfWeek.forEach {
                dayHeaderField(it)
            }
        }
    }

    fun dayHeaderField(
        state: LinCalendarState,
        modifier: Modifier = Modifier,
    ): @Composable RowScope.(dayOfWeek: DayOfWeek) -> Unit = { dayOfWeek ->
        val locale = remember { Locale.getDefault() }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .then(modifier),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, locale),
                style = TextStyle(),
            )
        }
    }

    fun weekField(
        state: LinCalendarState,
        modifier: Modifier = Modifier,
        dayField: @Composable RowScope.(yearMonth: YearMonth, localDate: LocalDate) -> Unit = dayField(
            state = state,
        )
    ): @Composable AnimatedVisibilityScope.(
        yearMonth: YearMonth,
        /** 当前周的第一天 */
        firstDateOfWeek: LocalDate,
    ) -> Unit = { yearMonth, firstDateOfWeek ->
        Row(
            modifier = Modifier.then(modifier),
        ) {
            for (i in 0 until 7) {
                val localDate = firstDateOfWeek.plusDays(i.toLong())
                dayField(yearMonth, localDate)
            }
        }
    }

    fun dayField(
        state: LinCalendarState,
        modifier: Modifier = Modifier,
    ): @Composable RowScope.(
        yearMonth: YearMonth,
        localDate: LocalDate,
    ) -> Unit = { yearMonth, localDate ->
        val now = remember { LocalDate.now() }

        Box(
            modifier = Modifier
                .height(state.option.rowHeight)
                .weight(1f)
                .clickable(
                    onClick = { state.date = localDate },
                    indication = rememberRipple(bounded = false), // 使用 Ripple 效果
                    interactionSource = remember { MutableInteractionSource() }
                )
                .then(modifier),
            contentAlignment = Alignment.Center,
        ) {

            val primary = MaterialTheme.colorScheme.primary
            if (localDate == state.date) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val diameter = min(size.width, size.height)
                    val radius = diameter / 2
                    drawCircle(
                        color = primary,
                        radius = radius,
                        center = Offset(size.width / 2, size.height / 2),
                        style = Stroke(1.dp.toPx())
                    )
                }
            }
            Text(
                text = localDate.dayOfMonth.toString(),
                style = TextStyle(
                    color = if (localDate == now) primary
                    else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (localDate == now) FontWeight.Black
                    else if (yearMonth.month == localDate.month) FontWeight.Normal
                    else FontWeight.Light,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MonthsFieldPreview() {
    val state = rememberLinCalendarState()
    LinCalendarDefaults.monthsField(state)()
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun MonthFieldPreview() {
    val state = rememberLinCalendarState()
    LazyRow {
        item {
            LinCalendarDefaults.monthField(state)(
                LocalDate.now(),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun MonthFieldPreview2() {
    val state = rememberLinCalendarState()
    LazyRow {
        item {
            LinCalendarDefaults.monthField(state)(
                LocalDate.of(2021, 2, 1),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeekHeaderFieldPreview() {
    val state = rememberLinCalendarState()
    Column {
        LinCalendarDefaults.weekHeaderField(state)()
    }
}

@Preview(showBackground = true)
@Composable
private fun DayFieldPreview() {
    val state = rememberLinCalendarState()
    Row {
        LinCalendarDefaults.dayField(state)(
            YearMonth.now(),
            LocalDate.now(),
        )
    }
}