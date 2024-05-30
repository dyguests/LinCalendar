package com.fanhl.lincalendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberLinCalendarState(
    initialPeriod: LocalDate = LocalDate.now(),
): LinCalendarState {
    val initialPage = 1
    val pagerState = rememberPagerState(
        initialPage = initialPage,
    ) { 3 }

    // todo 基于 option mode 可能是 当月第一天， 也可能是当周第一天。 其中当周第一天需要基于 firstDayOfWeek 来计算
    val formatInitialPeriod = YearMonth.from(initialPeriod).atDay(1)

    val calendarState = rememberSaveable(saver = LinCalendarStateImpl.Saver) {

        LinCalendarStateImpl(
            initialPeriod = formatInitialPeriod,
            initialPage = initialPage,
            pagerState = pagerState,
            // options = options,
        )
    }

    LaunchedEffect(calendarState.period) {
        val yearMonth = YearMonth.from(calendarState.period)
        val page = YearMonth.from(formatInitialPeriod).until(yearMonth, ChronoUnit.MONTHS).toInt() + 1
        if (page != pagerState.currentPage) {
            pagerState.scrollToPage(page)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        // todo 如果是 周视图，这里就应该计算周
        val changedPeriod = calendarState.getPeriodByPage(pagerState.currentPage)
        if (changedPeriod != calendarState.period) {
            calendarState.period = changedPeriod
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
    abstract var period: LocalDate

    @OptIn(ExperimentalFoundationApi::class)
    internal abstract val pagerState: PagerState

    internal abstract fun getPeriodByPage(page: Int): LocalDate
}

@OptIn(ExperimentalFoundationApi::class)
internal class LinCalendarStateImpl(
    private val initialPeriod: LocalDate,
    private val initialPage: Int,
    override val pagerState: PagerState,
) : LinCalendarState() {

    private var _period by mutableStateOf(initialPeriod)
    override var period: LocalDate
        get() = _period
        set(value) {
            _period = value
        }

    override fun getPeriodByPage(page: Int): LocalDate {
        // todo 未兼容 周时期
        val destMonth = YearMonth.from(initialPeriod).plusMonths((page - initialPage).toLong())
        if (destMonth == YearMonth.from(period)) {
            return period
        }
        val destDate = destMonth.atDay(1)
        return destDate
    }

    companion object {
        val Saver = Saver<LinCalendarStateImpl, Map<String, Any>>(
            save = {
                mapOf(
                    "initialPeriod" to it.initialPeriod,
                    "initialPage" to it.initialPage,
                    "pagerState" to it.pagerState,
                )
            },
            restore = {
                LinCalendarStateImpl(
                    initialPeriod = it["initialPeriod"] as LocalDate,
                    initialPage = it["initialPage"] as Int,
                    pagerState = it["pagerState"] as PagerState,
                )
            }
        )
    }
}