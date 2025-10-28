plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.safeword"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.safeword"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val keystorePath = (project.findProperty("ANDROID_KEYSTORE_PATH") as? String)?.takeIf { it.isNotBlank() }
    val keystorePassword = (project.findProperty("ANDROID_KEYSTORE_PASSWORD") as? String)?.takeIf { it.isNotBlank() }
    val keyAlias = (project.findProperty("ANDROID_KEY_ALIAS") as? String)?.takeIf { it.isNotBlank() }
    val keyPassword = (project.findProperty("ANDROID_KEY_PASSWORD") as? String)?.takeIf { it.isNotBlank() }
    val releaseSigningConfigured =
        listOf(keystorePath, keystorePassword, keyAlias, keyPassword).all { it != null } &&
            keystorePath?.let { file(it).exists() } == true

    if (releaseSigningConfigured) {
        signingConfigs {
            create("release") {
                storeFile = file(keystorePath!!)
                storePassword = keystorePassword
                this.keyAlias = keyAlias
                this.keyPassword = keyPassword
            }
        }
    }

    flavorDimensions += "edition"
    productFlavors {
        create("free") {
            dimension = "edition"
            applicationIdSuffix = ".free"
            versionNameSuffix = "-free"
            buildConfigField("boolean", "FEATURE_ADS_ENABLED", "true")
            buildConfigField("long", "RINGER_RESTORE_DELAY_MINUTES", "10L")
            buildConfigField("int", "CONTACT_LIMIT", "3")
        }
        create("pro") {
            dimension = "edition"
            applicationIdSuffix = ".pro"
            versionNameSuffix = "-pro"
            buildConfigField("boolean", "FEATURE_ADS_ENABLED", "false")
            buildConfigField("long", "RINGER_RESTORE_DELAY_MINUTES", "5L")
            buildConfigField("int", "CONTACT_LIMIT", "-1")
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (releaseSigningConfigured) {
                signingConfig = signingConfigs.getByName("release")
            } else {
                println("Android release signing config not provided; release artifacts will be unsigned.")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":shared"))

    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.window.size)
    implementation(libs.androidx.compose.material.icons.extended)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.accompanist.permissions)
    implementation(libs.play.services.location)
    implementation("com.google.android.gms:play-services-ads:23.2.0")

    // Optional networking/logging for peer bridge
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.logging)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    testImplementation(kotlin("test"))
}
