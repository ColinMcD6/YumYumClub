plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs")
}

android {
    namespace = "comp3350.yumyumclub"
    compileSdk = 34

    defaultConfig {
        applicationId = "comp3350.yumyumclub"
        minSdk = 33
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    testImplementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.navigation.runtime)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.androidx.espresso.contrib)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation("org.mockito:mockito-core:4.0.0")
    androidTestImplementation("org.mockito:mockito-android:4.0.0")
    androidTestImplementation("androidx.test:runner:1.6.1")
    androidTestImplementation("androidx.test:rules:1.6.1")
    implementation("androidx.compose.material:material-icons-extended:1.6.8")
    implementation("com.google.android.material:material:1.12.0");
    implementation("androidx.transition:transition:1.5.0");
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.7.7")
    implementation("androidx.compose.material:material-icons-extended:1.6.7")
    implementation("org.hsqldb:hsqldb:2.4.1")
    testImplementation("com.google.guava:guava:25.1-jre")
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(project(":app"))
    androidTestImplementation("junit:junit:4.12")
}

