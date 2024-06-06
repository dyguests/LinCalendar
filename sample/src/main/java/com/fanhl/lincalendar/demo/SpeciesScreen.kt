package com.fanhl.lincalendar.demo

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fanhl.lincalendar.LinCalendar
import com.fanhl.lincalendar.LinCalendarDefaults
import com.fanhl.lincalendar.LinCalendarState
import com.fanhl.lincalendar.demo.ui.theme.LinCalendarTheme
import com.fanhl.lincalendar.rememberLinCalendarState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun SpeciesScreen() {
    val state = rememberLinCalendarState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(8.dp)
    ) {
        item { DefaultCalendar(state) }
        item { TitledCalendar(state) }
        item { CustomCalendar() }
        item { HighlightCalendar(state) }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpeciesScreenPreview() {
    LinCalendarTheme {
        SpeciesScreen()
    }
}

@Composable
private fun DefaultCalendar(state: LinCalendarState) {
    Card(
        modifier = Modifier.padding(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Text(
                text = "Default",
            )
            LinCalendar(
                state = state,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun TitledCalendar(state: LinCalendarState) {
    Card(
        modifier = Modifier.padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = { state.date = state.date.plusMonths(-1) }) {
                    Text(text = "Last")
                }
                Text(text = state.date.toString())
                TextButton(onClick = { state.date = state.date.plusMonths(1) }) {
                    Text(text = "Next")
                }
            }
            LinCalendar(
                state = state,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CustomCalendar() {
    val state = rememberLinCalendarState(
        option = LinCalendarDefaults.option(
            headerHeight = 48.dp,
            rowHeight = 64.dp,
            firstDayOfWeek = DayOfWeek.WEDNESDAY,
        )
    )
    Card(
        modifier = Modifier.padding(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Text(
                text = "Custom Month",
            )
            LinCalendar(
                state = state,
                modifier = Modifier
                    .fillMaxWidth(),
                monthsField = customMonthsField(
                    state = state,
                    modifier = Modifier.background(Color(0x8DDF963D), CircleShape),
                    monthFiled = customMonthField(
                        state = state,
                        weekHeaderField = customWeekHeaderField(
                            state,
                            dayHeaderField = customDayHeaderField(state),
                        ),
                        weekField = customWeekField(
                            dayField = customDayField(state),
                        ),
                    ),
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomCalendarPreview() {
    LinCalendarTheme {
        CustomCalendar()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun customMonthsField(
    state: LinCalendarState,
    modifier: Modifier = Modifier,
    monthFiled: @Composable() (LazyItemScope.(date: LocalDate) -> Unit)
): @Composable () -> Unit = (@Composable {
    Box {
        Text(
            text = "Custom Months",
            style = TextStyle(
                color = Color(0xC93DDFB9),
                fontSize = 96.sp,
            ),
        )
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
                val date = state.getDateByPage(page)
                monthFiled(
                    date = date,
                )
            }
        }
    }
})

@Composable
private fun customMonthField(
    state: LinCalendarState,
    weekHeaderField: @Composable (ColumnScope.() -> Unit),
    weekField: @Composable AnimatedVisibilityScope.(yearMonth: YearMonth, firstDateOfWeek: LocalDate) -> Unit,
): @Composable LazyItemScope.(LocalDate) -> Unit = @Composable { date: LocalDate ->
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

    Box(
        modifier = Modifier
            .fillParentMaxWidth()
            .then(Modifier),
    ) {
        Text(
            text = "Custom Month ${YearMonth.from(date)}",
            modifier = Modifier.align(Alignment.BottomEnd),
            style = TextStyle(
                color = Color(0xC9894AD1),
                fontSize = 72.sp,
                fontWeight = FontWeight.Thin,
                textAlign = TextAlign.End,
            ),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
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
}

@Composable
private fun customWeekHeaderField(
    state: LinCalendarState,
    dayHeaderField: @Composable RowScope.(dayOfWeek: DayOfWeek) -> Unit
): @Composable ColumnScope.() -> Unit = {
    val sortedDaysOfWeek = remember {
        DayOfWeek.entries.run {
            slice(state.option.firstDayOfWeek.ordinal until size) + slice(0 until state.option.firstDayOfWeek.ordinal)
        }
    }
    Row(
        modifier = Modifier
            .height(state.option.headerHeight)
            .border(2.dp, Color(0xC9F84323))
            .then(Modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        sortedDaysOfWeek.forEach {
            dayHeaderField(it)
        }
    }
}

@Composable
private fun customDayHeaderField(
    state: LinCalendarState,
): @Composable RowScope.(dayOfWeek: DayOfWeek) -> Unit = { dayOfWeek ->
    val locale = remember { Locale.getDefault() }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .weight(dayOfWeek.value + 3f)
            .background(Color(0xC9F84323), CircleShape)
            .then(Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = when (dayOfWeek.value) {
                1 -> "1"
                2 -> "Ⅱ"
                3 -> "三"
                4 -> "THU"
                5 -> "friday"
                6 -> "星期六"
                else -> dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, locale)
            },
            style = TextStyle(),
        )
    }
}

@Composable
private fun customWeekField(
    dayField: @Composable RowScope.(yearMonth: YearMonth, localDate: LocalDate) -> Unit
): @Composable AnimatedVisibilityScope.(YearMonth, LocalDate) -> Unit =
    @androidx.compose.runtime.Composable { yearMonth: YearMonth, firstDateOfWeek: LocalDate ->
        Row(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x00F84323),
                            Color(0xC951E658),
                        ),
                        startY = 100f,
                    )
                )
                .then(Modifier),
        ) {
            for (i in 0 until 7) {
                val localDate = firstDateOfWeek.plusDays(i.toLong())
                dayField(yearMonth, localDate)
            }
        }
    }

@Composable
private fun customDayField(state: LinCalendarState): @Composable RowScope.(yearMonth: YearMonth, localDate: LocalDate) -> Unit =
    { yearMonth, localDate ->
        val context = LocalContext.current

        val now = remember { LocalDate.now() }

        Box(
            modifier = Modifier
                .height(state.option.rowHeight)
                .weight(1f)
                .then(Modifier),
            contentAlignment = Alignment.Center,
        ) {
            if (localDate.dayOfMonth == 1) {
                TextButton(onClick = { Toast.makeText(context, "${localDate.dayOfMonth}", Toast.LENGTH_SHORT).show() }) {
                    Text(text = localDate.dayOfMonth.toString())
                }
            } else if (localDate.dayOfMonth == 3) {
                Button(onClick = { Toast.makeText(context, "${localDate.dayOfMonth}", Toast.LENGTH_SHORT).show() }) {
                    Text(text = localDate.dayOfMonth.toString())
                }
            } else if (localDate.dayOfMonth == 5) {
                Text(
                    text = localDate.dayOfMonth.toString(),
                    style = TextStyle(
                        color = Color.Red,
                    ),
                )
            } else if (localDate.dayOfMonth == 7) {
                Text(
                    text = localDate.dayOfMonth.toString(),
                    style = TextStyle(
                        fontSize = 48.sp,
                    ),
                )
            } else if (localDate.dayOfMonth == 19) {
                Text(
                    text = "Custom\n${localDate.dayOfMonth}\nDay",
                    style = TextStyle(
                        fontSize = 12.sp,
                    ),
                )
            } else {
                Text(
                    text = localDate.dayOfMonth.toString(),
                    style = TextStyle(
                        color = if (localDate == now) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (localDate == now) FontWeight.Bold
                        else if (yearMonth.month == localDate.month) FontWeight.Normal
                        else FontWeight.Light,
                    ),
                )
            }
        }
    }


@Composable
private fun HighlightCalendar(state: LinCalendarState) {
    val dayField: @Composable RowScope.(YearMonth, LocalDate) -> Unit = { yearMonth: YearMonth, localDate: LocalDate ->
        val now = remember { LocalDate.now() }

        Box(
            modifier = Modifier
                .height(state.option.rowHeight)
                .weight(1f)
                .then(Modifier),
            contentAlignment = Alignment.Center,
        ) {
            if (localDate == now) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Red, CircleShape)
                )
            }
            Text(
                text = localDate.dayOfMonth.toString(),
                style = TextStyle(
                    color = if (localDate == now) Color.White
                    else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (localDate == now) FontWeight.Bold
                    else if (yearMonth.month == localDate.month) FontWeight.Normal
                    else FontWeight.Light,
                ),
            )
        }
    }

    Card(
        modifier = Modifier.padding(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Text(
                text = "Highlight Today",
            )
            LinCalendar(
                state = state,
                monthsField = LinCalendarDefaults.monthsField(
                    state = state,
                    monthFiled = LinCalendarDefaults.monthField(
                        state = state,
                        weekField = LinCalendarDefaults.weekField(
                            state = state,
                            dayField = dayField
                        ),
                    ),
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HighlightCalendarPreview() {
    LinCalendarTheme {
        HighlightCalendar(rememberLinCalendarState())
    }
}