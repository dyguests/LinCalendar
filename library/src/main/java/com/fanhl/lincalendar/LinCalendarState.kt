package com.fanhl.lincalendar

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import kotlin.properties.Delegates
import kotlin.random.Random

/**
 * @param initialDate 初始日期 用于判断当前显示的月份/周。（之前用YearMonth，但是无法兼容周视图，这里统一改成 LocalDate）
 */
@Composable
fun rememberLinCalendarState(
    initialDate: LocalDate = LocalDate.now(),
    startDate: LocalDate = LocalDate.of(1900, 1, 1),
    endDate: LocalDate = LocalDate.of(2099, 12, 31),
    initialDisplayMode: LinCalendar.DisplayMode = LinCalendar.DisplayMode.MONTHLY,
    option: LinCalendar.Option = LinCalendarDefaults.option(),
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
        initialDate.coerceIn(adjustedStartDate, adjustedEndDate)
    }

    // endregion ---------- adjusted ----------

    val state = rememberSaveable(saver = LinCalendarStateImpl.Saver) {
        LinCalendarStateImpl(
            initialDate = adjustedInitialDate,
            startDate = adjustedStartDate,
            endDate = adjustedEndDate,
            initialDisplayMode = initialDisplayMode,
            option = option,
        )
    }

    LaunchedEffect(state.date, state.displayMode) {
        val page = state.getPageByDate(state.date)
        if (state.listState.firstVisibleItemIndex == page) {
            return@LaunchedEffect
        }
        state.listState.scrollToItem(page)
    }

    LaunchedEffect(state.listState.firstVisibleItemIndex) {
        val date = state.getDateByPage(state.listState.firstVisibleItemIndex)
        if (state.date == date) {
            return@LaunchedEffect
        }
        state.date = date
    }

    return state
}

interface LinCalendarState {

    /**
     * 当前月/周视图对应的日期。
     */
    var date: LocalDate
    var displayMode: LinCalendar.DisplayMode
    val option: LinCalendar.Option

    val listState: LazyListState

    val pageCount: Int

    /**
     * 基于当前页数，计算对应的日期。
     */
    fun getDateByPage(page: Int): LocalDate

    /**
     * 基于日期，计算对应的页数。
     */
    fun getPageByDate(date: LocalDate): Int

    /**
     * LazyRow 的 key
     */
    fun getKey(page: Int): Any
}

@Stable
internal class LinCalendarStateImpl(
    initialDate: LocalDate,
    private val startDate: LocalDate,
    private val endDate: LocalDate,
    initialDisplayMode: LinCalendar.DisplayMode,
    override val option: LinCalendar.Option,
) : LinCalendarState {

    private var _date by mutableStateOf(initialDate)
    override var date: LocalDate
        get() = _date
        set(value) {
            if (_date == value) {
                return
            }

            _date = value.coerceIn(startDate, endDate)
        }

    private var _displayMode by mutableStateOf(initialDisplayMode)
    override var displayMode: LinCalendar.DisplayMode
        get() = _displayMode
        set(value) {
            if (_displayMode == value) {
                return
            }

            _displayMode = value

            _pageCount = calculatePageCount()
        }

    override val listState = LazyListState(firstVisibleItemIndex = getPageByDate(_date))

    private var _monthPageCount by Delegates.notNull<Int>()
    private var _weekPageCount by Delegates.notNull<Int>()

    init {
        _monthPageCount = ChronoUnit.MONTHS.between(YearMonth.from(startDate), YearMonth.from(endDate)).toInt() + 1

        val weekFields = WeekFields.of(option.firstDayOfWeek, 1)
        val startWeek = startDate.with(weekFields.dayOfWeek(), 1)
        val endWeek = endDate.with(weekFields.dayOfWeek(), 1)
        _weekPageCount = ChronoUnit.WEEKS.between(startWeek, endWeek).toInt() + 1
    }

    private var _pageCount by mutableStateOf(calculatePageCount())

    private fun calculatePageCount() = if (_displayMode == LinCalendar.DisplayMode.MONTHLY) {
        _monthPageCount
    } else {
        _weekPageCount
    }

    override val pageCount: Int
        get() = _pageCount

    override fun getDateByPage(page: Int): LocalDate {
        return if (_displayMode == LinCalendar.DisplayMode.MONTHLY) {
            val yearMonth = YearMonth.from(startDate).plusMonths(page.toLong())
            date.withYear(yearMonth.year).withMonth(yearMonth.monthValue)
        } else {
            startDate.plusWeeks(page.toLong())
                // 先找到这周的第一天
                .with(TemporalAdjusters.previousOrSame(option.firstDayOfWeek))
                // 再找到这周与date相同的周几
                .with(TemporalAdjusters.nextOrSame(date.dayOfWeek))
        }
    }

    override fun getPageByDate(date: LocalDate): Int {
        return if (_displayMode == LinCalendar.DisplayMode.MONTHLY) {
            ChronoUnit.MONTHS.between(YearMonth.from(startDate), YearMonth.from(date)).toInt()
        } else {
            val weekFields = WeekFields.of(option.firstDayOfWeek, 1)
            val startWeek = startDate.with(weekFields.dayOfWeek(), 1)
            ChronoUnit.WEEKS.between(startWeek, date.with(weekFields.dayOfWeek(), 1)).toInt()
        }
    }

    /**
     * key : month/week
     * value : LazyRow 的 key
     */
    private val monthKeyMap: MutableMap<YearMonth, Long> = mutableMapOf()
    private val weekKeyMap: MutableMap<LocalDate, Long> = mutableMapOf()
    override fun getKey(page: Int): Any {
        if (displayMode == LinCalendar.DisplayMode.MONTHLY) {
            val mapKey = YearMonth.from(getDateByPage(page))
            return monthKeyMap.getOrPut(mapKey) { Random.nextLong() }
        } else {
            val weekFields = WeekFields.of(option.firstDayOfWeek, 1)
            val mapKey = getDateByPage(page).with(weekFields.dayOfWeek(), 1) // 当周第一天
            return weekKeyMap.getOrPut(mapKey) { Random.nextLong() }
        }
    }

    companion object {
        val Saver = Saver<LinCalendarStateImpl, Map<String, Any>>(
            save = {
                mapOf(
                    "_date" to it._date,
                    "startDate" to it.startDate,
                    "endDate" to it.endDate,
                    "_displayMode" to it._displayMode,
                    "option" to it.option,
                )
            },
            restore = {
                LinCalendarStateImpl(
                    initialDate = it["_date"] as LocalDate,
                    startDate = it["startDate"] as LocalDate,
                    endDate = it["endDate"] as LocalDate,
                    initialDisplayMode = it["_displayMode"] as LinCalendar.DisplayMode,
                    option = it["option"] as LinCalendar.Option,
                )
            }
        )
    }
}