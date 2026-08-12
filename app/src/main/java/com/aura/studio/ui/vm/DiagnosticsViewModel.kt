package com.aura.studio.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.studio.monitor.RepairResult
import com.aura.studio.monitor.SystemMonitor
import com.aura.studio.monitor.SystemReport
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiagnosticsViewModel @Inject constructor(
    private val system: SystemMonitor
) : ViewModel() {
    private val _report = MutableStateFlow<SystemReport?>(null)
    val report: StateFlow<SystemReport?> = _report.asStateFlow()
    private val _busy = MutableStateFlow(false)
    val busy: StateFlow<Boolean> = _busy.asStateFlow()
    private val _lastRepair = MutableStateFlow<RepairResult?>(null)
    val lastRepair: StateFlow<RepairResult?> = _lastRepair.asStateFlow()
    private val _log = MutableStateFlow<List<String>>(emptyList())
    val log: StateFlow<List<String>> = _log.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _busy.value = true
            try {
                _report.value = system.report()
                append("Health scan complete")
            } catch (e: Exception) {
                append("Scan failed: ${e.message}")
            } finally {
                _busy.value = false
            }
        }
    }

    fun repair(actionId: String) {
        viewModelScope.launch {
            _busy.value = true
            try {
                val r = system.runRepair(actionId)
                _lastRepair.value = r
                append("${r.actionId}: ${r.message}")
                _report.value = system.report()
            } catch (e: Exception) {
                append("Repair error: ${e.message}")
            } finally {
                _busy.value = false
            }
        }
    }

    fun autoRepair() {
        viewModelScope.launch {
            _busy.value = true
            try {
                val results = system.autoRepair()
                results.forEach { append("${it.actionId}: ${it.message}") }
                _lastRepair.value = results.lastOrNull()
                _report.value = system.report()
            } catch (e: Exception) {
                append("Auto-repair error: ${e.message}")
            } finally {
                _busy.value = false
            }
        }
    }

    private fun append(line: String) {
        val ts = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.US).format(java.util.Date())
        _log.value = (_log.value + "[$ts] $line").takeLast(50)
    }
}
