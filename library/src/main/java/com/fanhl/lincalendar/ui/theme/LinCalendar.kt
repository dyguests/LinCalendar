package com.fanhl.lincalendar.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate

/**
 * @param localDate 当前显示日期；用于判断当前显示的月份/周。（之前用YearMonth，但是无法兼容周视图，这里统一改成 LocalDate）
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LinCalendar(
    localDate: LocalDate,
    modifier: Modifier = Modifier,
    mode: LinCalendar.Mode = LinCalendar.Mode.MONTH,
) {
    // 日期的初始展示的日期，后续Pager基于此日期计算分布
    val initLocalDate by remember { mutableStateOf(localDate) }
    val state = rememberPagerState(
        initialPage = 1,
    ) {
        3
    }
    HorizontalPager(
        state = state
    ) {
        Box(
            modifier = Modifier.size(300.dp, 240.dp),
        ) {
            Text(text = "Calendar")
        }
    }
}

@Preview(showBackground = true)
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