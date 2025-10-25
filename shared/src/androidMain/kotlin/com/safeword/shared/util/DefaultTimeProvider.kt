package com.safeword.shared.util

class DefaultTimeProvider : TimeProvider {
    override fun nowMillis(): Long = System.currentTimeMillis()
}

