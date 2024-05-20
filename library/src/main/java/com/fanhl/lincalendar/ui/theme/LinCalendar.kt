package com.fanhl.lincalendar.ui.theme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LinCalendar() {
    Text(text = "Calendar")
}

@Preview(showBackground = true)
@Composable
private fun LinCalendarPreview() {
    LinCalendar()
}