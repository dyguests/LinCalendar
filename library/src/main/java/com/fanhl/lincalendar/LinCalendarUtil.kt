package com.fanhl.lincalendar

import java.io.Serializable
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields

internal object LinCalendarUtil {
}

// fun LinCalendarState.getKey(page: Int): Any = getDateByPage(page).let { date ->
//     // return when (displayMode) {
//     //     LinCalendar.DisplayMode.MONTHLY -> YearMonth.from(date)
//     //     LinCalendar.DisplayMode.WEEKLY -> {
//     //         val weekFields = WeekFields.of(option.firstDayOfWeek, 1)
//     //         val year = date.get(weekFields.weekBasedYear())
//     //         val weekOfYear = date.get(weekFields.weekOfWeekBasedYear())
//     //         year to weekOfYear
//     //     }
//     // }
//
//     // 统一用 week 做 key，是为了 月/周视图切换时，不会因为 key 变化而重新创建 item，导致切换时没有动画效果
//     val weekFields = WeekFields.of(option.firstDayOfWeek, 1)
//     val year = date.get(weekFields.weekBasedYear())
//     val weekOfYear = date.get(weekFields.weekOfWeekBasedYear())
//     year to weekOfYear
// }

@Deprecated("不能用")
private class PeriodKey(
    val state: LinCalendarState,
    val date: LocalDate
) : Serializable {
    private val key: Any
        get() = when (state.displayMode) {
            LinCalendar.DisplayMode.MONTHLY -> monthKey
            LinCalendar.DisplayMode.WEEKLY -> weekKey
        }
    private val monthKey by lazy { YearMonth.from(date) }
    private val weekKey by lazy {
        val weekFields = WeekFields.of(state.option.firstDayOfWeek, 1)
        val year = date.get(weekFields.weekBasedYear())
        val weekOfYear = date.get(weekFields.weekOfWeekBasedYear())
        year to weekOfYear
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PeriodKey) return false
        return key == other.key
    }

    // override fun hashCode() = throw UnsupportedOperationException()
}