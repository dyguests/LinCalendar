package com.fanhl.lincalendar.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fanhl.lincalendar.LinCalendar
import com.fanhl.lincalendar.LinCalendarState
import com.fanhl.lincalendar.demo.ui.theme.LinCalendarTheme
import com.fanhl.lincalendar.rememberLinCalendarState

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
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                            composable(NAVI_INTERACTIONS) { InteractionsScreen() }
                        }
                    }
                }
            }
        }
    }

}

private const val NAVI_MAIN = "main"
private const val NAVI_SPECIES = "species"
private const val NAVI_INTERACTIONS = "interactions"

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainScreen(navController: NavHostController) {
    val state = rememberLinCalendarState()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Card(
            modifier = Modifier.padding(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = { state.date = state.date.plusMonths(-1) }) {
                        Text(text = "Last")
                    }
                    Text(text = state.date.toString())
                    TextButton(onClick = { state.date = state.date.plusMonths(1) }) {
                        Text(text = "Next")
                    }
                }
                LinCalendar(
                    state = state,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
        }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SpeciesScreen() {
    val state = rememberLinCalendarState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(8.dp)
    ) {
        item { DefaultLinCalendar(state) }
        item { CustomMonthLinCalendar(state) }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpeciesScreenPreview() {
    LinCalendarTheme {
        SpeciesScreen()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DefaultLinCalendar(state: LinCalendarState) {
    Card(
        modifier = Modifier.padding(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Text(
                text = "Default",
            )
            LinCalendar(
                state = state,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CustomMonthLinCalendar(state: LinCalendarState) {
    Card(
        modifier = Modifier.padding(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Text(
                text = "Custom Month",
            )
            LinCalendar(
                state = state,
                modifier = Modifier
                    .fillMaxWidth(),
                // todo
            )
        }
    }
}

@Composable
private fun InteractionsScreen() {
    val state = rememberLinCalendarState()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(8.dp)
    ) {
        item { ExpandableLinCalendar(state) }
    }
}

@Preview(showBackground = true)
@Composable
private fun InteractionsScreenPreview() {
    LinCalendarTheme {
        InteractionsScreen()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandableLinCalendar(state: LinCalendarState) {
    Card(
        modifier = Modifier.padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = { state.date = state.date.plusMonths(-1) }) {
                    Text(text = "Last")
                }
                Text(text = state.date.toString())
                TextButton(onClick = { state.date = state.date.plusMonths(1) }) {
                    Text(text = "Next")
                }
                TextButton(onClick = {
                    state.displayMode = if (state.displayMode == LinCalendar.DisplayMode.MONTHLY) LinCalendar.DisplayMode.WEEKLY
                    else LinCalendar.DisplayMode.MONTHLY
                }) {
                    Text(
                        text = if (state.displayMode == LinCalendar.DisplayMode.MONTHLY) "MONTHLY"
                        else "WEEKLY"
                    )
                }
            }
            LinCalendar(
                state = state,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ExpandableLinCalendarPreview() {
    val calendarState = rememberLinCalendarState()
    LinCalendarTheme {
        ExpandableLinCalendar(calendarState)
    }
}