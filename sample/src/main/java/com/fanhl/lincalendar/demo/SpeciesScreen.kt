package com.fanhl.lincalendar.demo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
        item { CustomMonthCalendar(state) }
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
private fun CustomMonthCalendar(state: LinCalendarState) {
    val option = LinCalendarDefaults.option(
        headerHeight = 48.dp,
        rowHeight = 64.dp,
        firstDayOfWeek = DayOfWeek.TUESDAY,
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
                option = option,
                monthsField = customMonthsField(
                    state = state,
                    modifier = Modifier.background(Color(0x8DDF963D), CircleShape),
                    monthFiled = LinCalendarDefaults.monthField(
                        option = option,
                    )
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomMonthCalendarPreview() {
    val state = rememberLinCalendarState()
    LinCalendarTheme {
        CustomMonthCalendar(state)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun customMonthsField(
    state: LinCalendarState,
    modifier: Modifier = Modifier,
    monthFiled: @Composable() (LazyItemScope.(state: LinCalendarState, date: LocalDate) -> Unit)
): @Composable () -> Unit = (@Composable {
    Box {
        Text(
            text = "Custom Months",
            style = TextStyle(
                color = Color(0xC93DDFB9),
                fontSize = 64.sp,
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
                    state = state,
                    date = date,
                )
            }
        }
    }
})
