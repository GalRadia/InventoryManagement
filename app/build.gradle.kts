plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.inventorymanagement"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.inventorymanagement"
        minSdk = 26
        targetSdk = 35
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(project(":InventoryManagementSDK"))
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.annotation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.lottie) //lottie animation
    implementation(libs.recyclerview)
    implementation(libs.cardview)
    implementation(libs.lifecycle.extensions)
    //glide
    implementation(libs.glide)

    implementation(libs.swiperefreshlayout)
    implementation(libs.mpandroidchart)
    implementation("androidx.core:core-splashscreen:1.0.1") // SplashScreen API
    implementation("com.airbnb.android:lottie:6.1.0")       // Lottie Animation




}