package com.fanhl.lincalendar.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate

object LinCalendarDefaults {
    @OptIn(ExperimentalFoundationApi::class)
    fun monthField(
        modifier: Modifier = Modifier,
    ): @Composable PagerScope.(selectedDate: LocalDate?) -> Unit = { selectedDate ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier),
        ) {
            Text(text = "Calendar")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true, widthDp = 300, heightDp = 240)
@Composable
private fun MonthFieldPreview() {
    HorizontalPager(state = rememberPagerState { 1 }) {
        LinCalendarDefaults.monthField()(LocalDate.now())

    }
}