plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    kotlin("kapt")
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.cryptox"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.cryptox"
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        val deepLinkHost = "periodically-round-appear-output.trycloudflare.com"
        manifestPlaceholders["deepLinkHost"] = deepLinkHost
        buildConfigField(
            "String",
            "DEEP_LINK_HOST",
            "\"$deepLinkHost\""
        )

        buildConfigField(
            "String",
            "WEB_CLIENT_ID",
            "\"1067464921442-trcnb3slk63h4epnt9vmgetunor68gab.apps.googleusercontent.com\""
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.datastore.core)
    //implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    //OkHttp
    implementation(libs.okhttp)
    //HILT - Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    //Navigation Compose
    implementation(libs.androidx.navigation.compose)
    //Pager Accompanist
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    //Coil Image
    implementation(libs.coil.compose)
    //Extended Icons
    implementation(libs.androidx.compose.material.icons.extended)
    //Datastore Preferences
    implementation(libs.androidx.datastore.preferences)
    //Firebase
    implementation(platform(libs.firebase.bom))
    //FirebaseAuth
    implementation(libs.firebase.auth)
    //Firebase FireStore
    implementation(libs.firebase.firestore)
    implementation(libs.kotlinx.coroutines.play.services)
    //Firebase Storage
    implementation(libs.firebase.storage)
    //Swipeable Items
    implementation(libs.swipe)
    //Credential Manager
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    //Google Id Token
    implementation(libs.googleid)
    //Material
    implementation(libs.androidx.material3)
    implementation(libs.google.material)
    //Country Code Picker
    implementation(libs.country.code.picker.compose)
    //HomeScreen Widget
    implementation(libs.glance.appwidget)
    implementation(libs.glance.material3)
}