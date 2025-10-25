package com.safeword.shared.util

import com.safeword.shared.domain.DashboardState
import com.safeword.shared.domain.SafeWordEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StateFlowAdapter<T>(
    private val scope: CoroutineScope,
    private val flow: StateFlow<T>
) {
    fun collect(onEach: (T) -> Unit): Job = scope.launch {
        flow.collect { value -> onEach(value) }
    }

    fun current(): T = flow.value
}

fun <T> StateFlow<T>.asAdapter(scope: CoroutineScope): StateFlowAdapter<T> =
    StateFlowAdapter(scope, this)

fun SafeWordEngine.dashboardAdapter(scope: CoroutineScope): StateFlowAdapter<DashboardState> =
    this.dashboardState.asAdapter(scope)
