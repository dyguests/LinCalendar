package com.fanhl.lincalendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun rememberLinCalendarState(
    initialPeriod: LocalDate = LocalDate.now(),
    // initialSelectedDate: LocalDate? = null,
): LinCalendarState {
    return rememberSaveable(saver = LinCalendarStateImpl.Saver) {
        // todo 基于 option mode 可能是 当月第一天， 也可能是当周第一天。 其中当周第一天需要基于 firstDayOfWeek 来计算
        val formatInitialPeriod = YearMonth.from(initialPeriod).atDay(1)

        LinCalendarStateImpl(
            initialPeriod = formatInitialPeriod,
            // initialSelectedDate = initialSelectedDate,
            // options = options,
        )
    }
}

abstract class LinCalendarState {
    /**
     * 当前显示的时期。
     *
     * 用于判断当前显示的月份/周。
     * 实际值为 当月第一天/当周第一天。
     */
    abstract val currentPeriod: LocalDate

    internal abstract fun updatePeriod(period: LocalDate)
}

internal class LinCalendarStateImpl(
    private val initialPeriod: LocalDate,
) : LinCalendarState() {

    private var period by mutableStateOf(initialPeriod)
    override val currentPeriod: LocalDate
        get() = period

    override fun updatePeriod(period: LocalDate) {
        this.period = period
    }

    companion object {
        val Saver = Saver<LinCalendarStateImpl, Map<String, Any>>(
            save = {
                mapOf(
                    "period" to it.period,
                )
            },
            restore = {
                LinCalendarStateImpl(
                    initialPeriod = it["period"] as LocalDate,
                )
            }
        )
    }
}