package com.fanhl.lincalendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

/**
 * @param initialDate 初始日期 用于判断当前显示的月份/周。（之前用YearMonth，但是无法兼容周视图，这里统一改成 LocalDate）
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberLinCalendarState(
    initialDate: LocalDate = LocalDate.now(),
    startDate: LocalDate = LocalDate.of(1900, 1, 1),
    endDate: LocalDate = LocalDate.of(2099, 12, 31),
    initialDisplayMode: LinCalendar.DisplayMode = LinCalendar.DisplayMode.MONTHLY,
): LinCalendarState {
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

    val initialPage = YearMonth.from(adjustedStartDate).until(adjustedInitialDate, ChronoUnit.MONTHS).toInt()
    val pagerState = rememberPagerState(
        initialPage = initialPage,
    ) { YearMonth.from(adjustedStartDate).until(adjustedEndDate, ChronoUnit.MONTHS).toInt() + 1 }

    val calendarState = rememberSaveable(saver = LinCalendarStateImpl.Saver) {
        LinCalendarStateImpl(
            initialDate = adjustedInitialDate,
            initialDisplayMode = initialDisplayMode,
            initialPage = initialPage,
            pagerState = pagerState,
        )
    }

    LaunchedEffect(calendarState.date) {
        val yearMonth = YearMonth.from(calendarState.date)
        val changedPage = initialPage + YearMonth.from(adjustedInitialDate).until(yearMonth, ChronoUnit.MONTHS).toInt()
        if (changedPage != pagerState.currentPage) {
            pagerState.scrollToPage(changedPage)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        // todo 如果是 周视图，这里就应该计算周
        val changedPeriod = calendarState.getDateByPage(pagerState.currentPage)
        if (changedPeriod != calendarState.date) {
            calendarState.date = changedPeriod
        }
    }

    return calendarState
}

abstract class LinCalendarState {
    /**
     * 当前显示的时期。
     *
     * 用于判断当前显示的月份/周。
     * 实际值为 当月第一天/当周第一天。
     */
    abstract var date: LocalDate
    abstract var displayMode: LinCalendar.DisplayMode

    @OptIn(ExperimentalFoundationApi::class)
    internal abstract val pagerState: PagerState

    internal abstract fun getDateByPage(page: Int): LocalDate
}

@OptIn(ExperimentalFoundationApi::class)
internal class LinCalendarStateImpl(
    private val initialDate: LocalDate,
    private val initialDisplayMode: LinCalendar.DisplayMode,
    private val initialPage: Int,
    override val pagerState: PagerState,
) : LinCalendarState() {

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

    override fun getDateByPage(page: Int): LocalDate {
        // todo 未兼容 周时期
        val destMonth = YearMonth.from(initialDate).plusMonths((page - initialPage).toLong())
        if (destMonth == YearMonth.from(date)) {
            return date
        }
        val destDate = date.withYear(destMonth.year).withMonth(destMonth.monthValue)
        return destDate
    }

    companion object {
        val Saver = Saver<LinCalendarStateImpl, Map<String, Any>>(
            save = {
                mapOf(
                    "initialDate" to it.initialDate,
                    "initialDisplayMode" to it.initialDisplayMode,
                    "initialPage" to it.initialPage,
                    "pagerState" to it.pagerState,
                )
            },
            restore = {
                LinCalendarStateImpl(
                    initialDate = it["initialDate"] as LocalDate,
                    initialDisplayMode = it["initialDisplayMode"] as LinCalendar.DisplayMode,
                    initialPage = it["initialPage"] as Int,
                    pagerState = it["pagerState"] as PagerState,
                )
            }
        )
    }
}