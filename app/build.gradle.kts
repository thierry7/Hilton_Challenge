plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.hilton_challenge" // Set your namespace here
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hilton_challenge"
        minSdkVersion(24)
        targetSdkVersion(34)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        excludes.add("META-INF/gradle/incremental.annotation.processors")
    }
    testOptions{
        unitTests {
            isIncludeAndroidResources = true

        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.hilt.android)
    annotationProcessor(libs.hilt.android.compiler)
    implementation(libs.retrofit)
    implementation(libs.room.runtime)
    implementation(libs.gson)
    implementation(libs.converter.gson)
    annotationProcessor (libs.room.compiler)
    implementation(libs.logging.interceptor)
    implementation(libs.junit)
    implementation(libs.mockito.core)
    implementation(libs.mockwebserver)
    implementation(libs.core.testing)
    annotationProcessor(libs.annotation)
    testImplementation(libs.robolectric)
    testImplementation(libs.hilt.android.testing)
}
