package com.fanhl.lincalendar.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
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

/**
 * @param localDate 当前显示日期；用于判断当前显示的月份/周。（之前用YearMonth，但是无法兼容周视图，这里统一改成 LocalDate）
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LinCalendar(
    localDate: LocalDate,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
    firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    mode: LinCalendar.Mode = LinCalendar.Mode.MONTH,
    monthFiled: @Composable PagerScope.(selectedDate: LocalDate?) -> Unit = remember {
        LinCalendarDefaults.monthField(
            firstDayOfWeek = firstDayOfWeek,
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
            .then(modifier),
    ) {
        // mode==LinCalendar.Mode.MONTH // todo
        monthFiled(
            selectedDate
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, widthDp = 320, heightDp = 240)
@Composable
private fun LinCalendarPreview() {
    LinCalendar(
        localDate = LocalDate.now(),
    )
}

object LinCalendar {
    enum class Mode {
        MONTH,
        WEEK,
    }
}