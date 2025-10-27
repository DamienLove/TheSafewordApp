plugins {
    kotlin("multiplatform") version "2.0.0" apply false
    kotlin("android") version "2.0.0" apply false
    id("com.android.application") version "8.3.2" apply false
    id("com.android.library") version "8.3.2" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
}

tasks.register("printProjectLayout") {
    group = "help"
    description = "Lists project modules for quick inspection."
    doLast {
        println("Modules:")
        rootProject.subprojects.forEach { println(" - ${it.path}") }
    }
}
