package com.fanhl.lincalendar.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fanhl.lincalendar.demo.ui.theme.LinCalendarTheme

@Composable
fun MainScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // TitledCalendar(rememberLinCalendarState())
        TextButton(onClick = { navController.navigate(NAVI_SPECIES) }) {
            Text(text = "Species")
        }
        TextButton(onClick = { navController.navigate(NAVI_INTERACTIONS) }) {
            Text(text = "Interactions")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    LinCalendarTheme {
        MainScreen(rememberNavController())
    }
}