package com.fanhl.lincalendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LinCalendar(
    state: LinCalendarState,
    modifier: Modifier = Modifier,
    options: LinCalendar.Option = LinCalendarDefaults.option(),
    monthFiled: @Composable PagerScope.(
        state: LinCalendarState,
        date: LocalDate
    ) -> Unit = LinCalendarDefaults.monthField(
        options = options, // todo 后续看能否通过 LinCalendarScope 来透传
    ),
) {
    HorizontalPager(
        state = state.pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .then(modifier),
        beyondBoundsPageCount = 1,
    ) { page ->
        // mode==LinCalendar.Mode.MONTH // todo
        monthFiled(
            state,
            state.getDateByPage(page),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun LinCalendarPreview() {
    val state = rememberLinCalendarState()
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = state.date.toString())
        }
        LinCalendar(
            state = state,
        )
    }
}

object LinCalendar {
    data class Option(
        val headerHeight: Dp = 32.dp,
        val rowHeight: Dp = 36.dp,
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

    enum class DisplayMode {
        MONTHLY,
        WEEKLY,
    }
}