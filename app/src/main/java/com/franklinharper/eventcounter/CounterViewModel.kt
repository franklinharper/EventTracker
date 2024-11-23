package com.franklinharper.eventcounter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
class CounterViewModel : ViewModel() {
    private val _counts = MutableStateFlow<Map<String, String>>(emptyMap())
    val counts = _counts.asStateFlow()
    val events = mutableListOf<LocalDateTime>()

//    init {
//        viewModelScope.launch {
//            while (true) {
//                updateCounts()
//                delay(timeMillis = 1_000L) // 1 second
//            }
//        }
//    }

    init {
        viewModelScope.launch {
            tickerFlow(1_000L).collect {
                updateStats()
            }
        }
    }

    private fun tickerFlow(period: Long) = flow {
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

    fun addEvent() {
        events += LocalDateTime.now()
        updateStats()
    }

    fun resetEvents() {
        events.clear()
        updateStats()
    }

    fun updateStats() {
        val now = LocalDateTime.now()
        // There's no reason for this to be a Map! It should be a List.
        val newCounts = mutableMapOf<String, String>()
        val stepValue = 5
        for (startMinutes in 0..30 step stepValue) {
            val (label, value) = stats(
                startMinutes = startMinutes,
                endMinutes = startMinutes + stepValue,
                now = now,
                events = events
            )
            newCounts[label] = value
        }
        _counts.update {
            newCounts.toMap()
        }
    }

   fun stats(now: LocalDateTime, events: List<LocalDateTime>, startMinutes:Int, endMinutes: Int): Pair<String, String> {
//       val label = "${startMinutes}-${endMinutes} minutes"
       val label = String.format("%02d", startMinutes) + "-" + String.format("%02d", endMinutes) + ": "
       val value = events.count { ChronoUnit.MINUTES.between(it, now) in startMinutes..<endMinutes }
       return label to "*".repeat(value)
   }
}