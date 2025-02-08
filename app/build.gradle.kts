plugins {
    // Add firebase related dependencies
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.audioanalysis"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.audioanalysis"
        minSdk = 29
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Adding firebase related dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.8.0")) // Firebase BOM

    // Add Firebase dependencies based on the features
    implementation("com.google.firebase:firebase-auth")       // Firebase Authentication
    implementation("com.google.firebase:firebase-firestore")  // Firestore Database
    implementation("com.google.firebase:firebase-storage")    // Firebase Storage
}