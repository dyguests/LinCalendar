package com.fanhl.lincalendar.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale

/**
 * @param localDate 当前显示日期；用于判断当前显示的月份/周。（之前用YearMonth，但是无法兼容周视图，这里统一改成 LocalDate）
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LinCalendar(
    localDate: LocalDate,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
    mode: LinCalendar.Mode = LinCalendar.Mode.MONTH,
    options: LinCalendar.Option = LinCalendarDefaults.defaultOption(),
    monthFiled: @Composable() (PagerScope.(localDate: LocalDate, selectedDate: LocalDate?) -> Unit) = remember {
        LinCalendarDefaults.monthField(
            firstDayOfWeek = options.firstDayOfWeek,
            // weekHeaderField = LinCalendarDefaults.weekHeaderField(firstDayOfWeek = firstDayOfWeek)
        )
    },
) {
    // 日期的初始展示的日期，后续Pager基于此日期计算分布
    val initLocalDate by remember { mutableStateOf(localDate) }
    val state = rememberPagerState(
        initialPage = 1,
    ) {
        3
    }
    HorizontalPager(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(modifier),
        beyondBoundsPageCount = 1,
    ) {
        // mode==LinCalendar.Mode.MONTH // todo
        monthFiled(
            initLocalDate,
            selectedDate
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun LinCalendarPreview() {
    LinCalendar(
        localDate = LocalDate.now(),
    )
}

object LinCalendar {
    data class Option(
        val firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
        val weekDisplayMode: WeekDisplayMode = WeekDisplayMode.FIXED_HEIGHT,
        val locale: Locale = Locale.getDefault(),
    )

    /**
     * 大多数情况下一个月有5周，极端情况会出现一个月仅有4周。
     * 例如 2009年2月
     * 这里配置 Calendar 的高度兼容模式
     */
    enum class WeekDisplayMode {
        // 高度不变，空余第五周
        FIXED_HEIGHT,

        // 高度不变，按周平分高度
        EQUAL_HEIGHT,

        // 高度按当月有几周来动态适配
        DYNAMIC_HEIGHT
    }

    enum class Mode {
        MONTH,
        WEEK,
    }
}