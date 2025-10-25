package com.safeword.shared.util

import kotlinx.datetime.Clock

class DefaultTimeProvider : TimeProvider {
    override fun nowMillis(): Long = Clock.System.now().toEpochMilliseconds()
}

