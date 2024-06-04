package com.fanhl.lincalendar.demo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fanhl.lincalendar.LinCalendar
import com.fanhl.lincalendar.LinCalendarState
import com.fanhl.lincalendar.demo.ui.theme.LinCalendarTheme
import com.fanhl.lincalendar.rememberLinCalendarState
import java.time.YearMonth

@Composable
fun InteractionsScreen() {
    val now = YearMonth.now()
    val state = rememberLinCalendarState(
        startDate = now.plusMonths(-1).atDay(1),
        endDate = now.plusMonths(1).atEndOfMonth(),
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(8.dp)
    ) {
        item { ExpandableLinCalendar(state) }
    }
}

@Preview(showBackground = true)
@Composable
private fun InteractionsScreenPreview() {
    LinCalendarTheme {
        InteractionsScreen()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandableLinCalendar(state: LinCalendarState) {
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
                TextButton(onClick = {
                    state.displayMode = if (state.displayMode == LinCalendar.DisplayMode.MONTHLY) LinCalendar.DisplayMode.WEEKLY
                    else LinCalendar.DisplayMode.MONTHLY
                }) {
                    Text(
                        text = if (state.displayMode == LinCalendar.DisplayMode.MONTHLY) "MONTHLY"
                        else "WEEKLY"
                    )
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

@Preview(showBackground = true)
@Composable
private fun ExpandableLinCalendarPreview() {
    val state = rememberLinCalendarState()
    LinCalendarTheme {
        ExpandableLinCalendar(state)
    }
}