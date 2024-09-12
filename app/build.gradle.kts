plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.histopath"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.histopath"
        minSdk = 28
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("androidx.activity:activity:1.8.0")
    implementation("com.google.firebase:firebase-storage:21.0.0")
    implementation("com.github.chrisbanes:PhotoView:2.3.0")
    implementation ("com.github.ybq:Android-SpinKit:1.4.0")
    implementation ("com.alexvasilkov:gesture-views:2.8.1")
    implementation ("nl.bryanderidder:themed-toggle-button-group:1.4.1")
    implementation ("com.github.AnyChart:AnyChart-Android:1.1.5")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}