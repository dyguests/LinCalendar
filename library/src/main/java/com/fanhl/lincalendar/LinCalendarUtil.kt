package com.fanhl.lincalendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.YearMonth
import java.time.temporal.WeekFields

internal object LinCalendarUtil {
}

fun LinCalendarState.getKey(page: Int): Any = getDateByPage(page).let { date ->
    return when (displayMode) {
        LinCalendar.DisplayMode.MONTHLY -> YearMonth.from(date)
        LinCalendar.DisplayMode.WEEKLY -> {
            val weekFields = WeekFields.of(option.firstDayOfWeek, 1)
            val year = date.get(weekFields.weekBasedYear())
            val weekOfYear = date.get(weekFields.weekOfWeekBasedYear())
            year to weekOfYear
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberLinFlingBehavior(
    state: LinCalendarState,
): FlingBehavior {
    // todo
    val monthBehavior = rememberSnapFlingBehavior(lazyListState = state.monthListState)
    val weekBehavior = rememberSnapFlingBehavior(lazyListState = state.weekListState)

    var behavior by remember { mutableStateOf(monthBehavior) }

    LaunchedEffect(state.displayMode) {
        behavior = when (state.displayMode) {
            LinCalendar.DisplayMode.MONTHLY -> monthBehavior
            LinCalendar.DisplayMode.WEEKLY -> weekBehavior
        }
    }

    return behavior
}