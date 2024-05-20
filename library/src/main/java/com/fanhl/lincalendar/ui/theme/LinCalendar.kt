package com.fanhl.lincalendar.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LinCalendar() {
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
    LinCalendar()
}