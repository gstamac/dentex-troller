plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.dentex.troller"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.dentex.troller"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
        // True in the sim build, whose remote and motor are simulated.
        buildConfigField("boolean", "SIMULATOR", "false")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        // For testing without the motor: a simulated remote and motor replace Bluetooth, so this
        // build cannot reach a real remote. Controlled with adb, see the readme. Installs over the debug app.
        create("sim") {
            initWith(getByName("debug"))
            matchingFallbacks += "debug"
            buildConfigField("boolean", "SIMULATOR", "true")
        }
    }

    // The link factory: the real one in src/real, the simulated one in src/sim (sim build only).
    sourceSets {
        getByName("debug").kotlin.directories += "src/real/kotlin"
        getByName("release").kotlin.directories += "src/real/kotlin"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":ble"))
    implementation(project(":haswing"))
    implementation(project(":logging"))
    implementation(project(":mavlink"))
    implementation(project(":navigation"))
    "simImplementation"(project(":simulator"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.maplibre.android)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
}
