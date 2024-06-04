package com.fanhl.lincalendar

import java.time.YearMonth
import java.time.temporal.WeekFields

internal object LinCalendarUtil {
}

fun LinCalendarState.getKey(page: Int): Any = getDateByPage(page).let { date ->
    return when (displayMode) {
        LinCalendar.DisplayMode.MONTHLY -> YearMonth.from(date)
        LinCalendar.DisplayMode.WEEKLY -> {
            val weekFields = WeekFields.of(firstDayOfWeek, 1)
            val year = date.get(weekFields.weekBasedYear())
            val weekOfYear = date.get(weekFields.weekOfWeekBasedYear())
            year to weekOfYear
        }
    }
}