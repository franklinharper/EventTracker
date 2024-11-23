package com.franklinharper.eventcounter

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {

    val viewModel: EventsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EventApp(viewModel)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EventApp(viewModel: EventsViewModel) {
        val counts by viewModel.eventData.collectAsState()

        Scaffold(topBar = {
            TopAppBar(title = { Text("Event Tracker") })
        }, content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { viewModel.addEvent() }) {
                        Text("+ Add Event")
                    }
                    Button(onClick = { viewModel.resetEvents() }) {
                        Text("Reset")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                // Stats
                Text("Events by time bucket", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(8.dp))
                counts.forEach { (timeframe, count) ->
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = "$timeframe $count",
                        style = MaterialTheme.typography.displaySmall,
                    )
                }
            }
        })
    }
}
