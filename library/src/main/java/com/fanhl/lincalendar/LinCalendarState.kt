package com.fanhl.lincalendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

/**
 * @param initialDate 初始日期 用于判断当前显示的月份/周。（之前用YearMonth，但是无法兼容周视图，这里统一改成 LocalDate）
 */
@Composable
fun rememberLinCalendarState(
    initialDate: LocalDate = LocalDate.now(),
    startDate: LocalDate = LocalDate.of(1900, 1, 1),
    endDate: LocalDate = LocalDate.of(2099, 12, 31),
    initialDisplayMode: LinCalendar.DisplayMode = LinCalendar.DisplayMode.MONTHLY,
    firstDayOfWeek: DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek,
): LinCalendarState {
    // region ---------- adjusted date ----------

    // Ensure startDate < endDate
    val (adjustedStartDate, adjustedEndDate) = remember(startDate, endDate) {
        if (endDate.isBefore(startDate)) {
            startDate to startDate
        } else {
            startDate to endDate
        }
    }

    // Ensure initialDate is within startDate and endDate
    val adjustedInitialDate = remember(startDate, endDate, initialDate) {
        when {
            initialDate.isBefore(adjustedStartDate) -> adjustedStartDate
            initialDate.isAfter(adjustedEndDate) -> adjustedEndDate
            else -> initialDate
        }
    }

    // endregion ---------- adjusted ----------

    return rememberSaveable(saver = LinCalendarStateImpl.Saver) {
        LinCalendarStateImpl(
            initialDate = adjustedInitialDate,
            startDate = adjustedStartDate,
            endDate = adjustedEndDate,
            initialDisplayMode = initialDisplayMode,
            firstDayOfWeek = firstDayOfWeek,
        )
    }

    // val initialPage = YearMonth.from(adjustedStartDate).until(adjustedInitialDate, ChronoUnit.MONTHS).toInt()
    // val pagerState = rememberPagerState(
    //     initialPage = initialPage,
    // ) { YearMonth.from(adjustedStartDate).until(adjustedEndDate, ChronoUnit.MONTHS).toInt() + 1 }
    //
    // val calendarState = rememberSaveable(saver = LinCalendarStateImpl.Saver) {
    //     LinCalendarStateImpl(
    //         initialDate = adjustedInitialDate,
    //         startDate = adjustedStartDate,
    //         endDate = adjustedEndDate,
    //         initialDisplayMode = initialDisplayMode,
    //         initialPage = initialPage,
    //         pagerState = pagerState,
    //     )
    // }
    //
    // LaunchedEffect(calendarState.date) {
    //     val yearMonth = YearMonth.from(calendarState.date)
    //     val changedPage = initialPage + YearMonth.from(adjustedInitialDate).until(yearMonth, ChronoUnit.MONTHS).toInt()
    //     if (changedPage != pagerState.currentPage) {
    //         pagerState.scrollToPage(changedPage)
    //     }
    // }
    //
    // LaunchedEffect(pagerState.currentPage) {
    //     // todo 如果是 周视图，这里就应该计算周
    //     val changedPeriod = calendarState.getDateByPage(pagerState.currentPage)
    //     if (changedPeriod != calendarState.date) {
    //         calendarState.date = changedPeriod
    //     }
    // }
    //
    // return calendarState
}

interface LinCalendarState {
    /**
     * 当前月/周视图对应的日期。
     */
    var date: LocalDate
    var displayMode: LinCalendar.DisplayMode
}

internal class LinCalendarStateImpl(
    initialDate: LocalDate,
    private val startDate: LocalDate,
    private val endDate: LocalDate,
    initialDisplayMode: LinCalendar.DisplayMode,
    private val firstDayOfWeek: DayOfWeek,
) : LinCalendarState {

    private var _date by mutableStateOf(initialDate)
    override var date: LocalDate
        get() = _date
        set(value) {
            _date = value
        }

    private var _displayMode by mutableStateOf(initialDisplayMode)
    override var displayMode: LinCalendar.DisplayMode
        get() = _displayMode
        set(value) {
            _displayMode = value
        }

    // todo see sample9

    companion object {
        val Saver = Saver<LinCalendarStateImpl, Map<String, Any>>(
            save = {
                mapOf(
                    "_date" to it._date,
                    "startDate" to it.startDate,
                    "endDate" to it.endDate,
                    "_displayMode" to it._displayMode,
                    "firstDayOfWeek" to it.firstDayOfWeek,
                )
            },
            restore = {
                LinCalendarStateImpl(
                    initialDate = it["_date"] as LocalDate,
                    startDate = it["startDate"] as LocalDate,
                    endDate = it["endDate"] as LocalDate,
                    initialDisplayMode = it["_displayMode"] as LinCalendar.DisplayMode,
                    firstDayOfWeek = it["firstDayOfWeek"] as DayOfWeek,
                )
            }
        )
    }
}