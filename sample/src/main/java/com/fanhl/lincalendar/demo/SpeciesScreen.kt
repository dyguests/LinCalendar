package com.fanhl.lincalendar.demo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.fanhl.lincalendar.getKey
import com.fanhl.lincalendar.rememberLinCalendarState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields

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
                        weekHeaderField = customWeekHeaderField(state),
                        weekField = customWeekField(state)
                    )
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
private fun customWeekHeaderField(state: LinCalendarState) = LinCalendarDefaults.weekHeaderField(
    state = state,
    dayHeaderField = customDayHeaderField(state)
)

@Composable
private fun customDayHeaderField(state: LinCalendarState) = LinCalendarDefaults.dayHeaderField(
    state = state,
)

@Composable
private fun customWeekField(state: LinCalendarState) = LinCalendarDefaults.weekField(
    state = state,
    dayField = customDayField(state)
)

@Composable
private fun customDayField(state: LinCalendarState) = LinCalendarDefaults.dayField(
    state = state,
)
