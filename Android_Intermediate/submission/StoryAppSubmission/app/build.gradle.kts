plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    id("kotlin-android")
    id("com.google.devtools.ksp")
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.dicoding.storyappsubmission"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dicoding.storyappsubmission"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField ("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")
        buildConfigField ("String", "DATABASE_NAME", "\"story_database\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            // Aktifkan logging untuk memudahkan debugging
            buildConfigField("boolean", "LOG_ENABLED", "true")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.logging.interceptor)

    implementation(libs.androidx.lifecycle.runtime.ktx) // untuk lifecycleScope
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // untuk viewmodelScope)

    implementation(libs.androidx.datastore.preferences)  // untuk dataStore, simpan token login
    implementation(libs.com.google.code.gson)

    implementation(libs.com.github.bumptech.glide)

    implementation(libs.play.services.location) // untuk location

    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.room.paging)

    implementation(libs.androidx.room.ktx)
    ksp(libs.room.compiler)

    androidTestImplementation(libs.androidx.core.testing) //InstantTaskExecutorRule
    androidTestImplementation(libs.kotlinx.coroutines.test) //TestDispatcher

    testImplementation(libs.androidx.core.testing) // InstantTaskExecutorRule
    testImplementation(libs.kotlinx.coroutines.test) //TestDispatcher
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)

    androidTestImplementation(libs.espresso.intents) //IntentsTestRule
}