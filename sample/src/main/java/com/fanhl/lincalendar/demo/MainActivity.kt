package com.fanhl.lincalendar.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fanhl.lincalendar.LinCalendar
import com.fanhl.lincalendar.demo.ui.theme.LinCalendarTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            LinCalendarTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(text = "LinCalendar") },
                            navigationIcon = {
                                IconButton(onClick = { navController.navigateUp() }) {
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = "Localized description"
                                    )
                                }
                            }
                        )
                    },
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        NavHost(navController = navController, startDestination = NAVI_MAIN) {
                            composable(NAVI_MAIN) { MainScreen(navController) }
                            composable(NAVI_SPECIES) { SpeciesScreen() }
                        }
                    }
                }
            }
        }
    }

}

private const val NAVI_MAIN = "main"
private const val NAVI_SPECIES = "species"

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Card(
            modifier = Modifier.padding(8.dp),
        ) {
            var localDate by remember { mutableStateOf(LocalDate.now()) }
            LinCalendar(
                period = localDate,
                onPeriodChange = { localDate = it },
                modifier = Modifier
                    .fillMaxWidth()
                    // .height(240.dp)
                    .padding(8.dp),
            )
        }

        Button(onClick = { navController.navigate(NAVI_SPECIES) }) {
            Text(text = "Species")
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SpeciesScreen() {
    var localDate by remember { mutableStateOf(LocalDate.now()) }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(8.dp),
        ) {
            LinCalendar(
                period = localDate,
                onPeriodChange = { localDate = it },
                modifier = Modifier
                    .fillMaxWidth()
                    // .height(240.dp)
                    .padding(8.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpeciesScreenPreview() {
    LinCalendarTheme {
        SpeciesScreen()
    }
}