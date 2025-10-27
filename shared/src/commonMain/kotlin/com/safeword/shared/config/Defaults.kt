package com.safeword.shared.config

import com.safeword.shared.domain.model.SafeWordSettings

object Defaults {
    val settings = SafeWordSettings(
        safeWordOne = "",
        safeWordTwo = "",
        sensitivity = 50,
        listeningEnabled = false,
        includeLocation = true,
        playSiren = false,
        testMode = false
    )
}
