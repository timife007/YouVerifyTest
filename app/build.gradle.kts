import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.timife.youverifytest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.timife.youverifytest"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties().apply{
            load(project.rootProject.file("local.properties").inputStream())
        }
        buildConfigField("String","PUBLIC_KEY","\"${properties.getProperty("public-key")}\"")
        buildConfigField("String","SECRET_KEY","\"${properties.getProperty("secret-key")}\"")

    }
    buildFeatures{
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")

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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.roomKtx)
    implementation(libs.androidx.room)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)
    ksp(libs.androidx.roomCompiler)
    implementation(libs.coroutineCore)
    implementation(libs.coroutineAndroid)
    implementation(libs.androidx.hilt.navigation.fragment)
    ksp(libs.dagger.hilt.compiler)
    implementation(libs.hilt)
    implementation(libs.timber)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

    //Network Http connection
    implementation (libs.retrofit)
    implementation(libs.okhttp3.okhttp)
    implementation(libs.okhttp3.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit2.kotlin.coroutines.adapter)
    implementation(libs.converter.gson)

    //Paystack
    implementation(libs.paystack.ui)

    //json
    implementation(libs.gson)
    implementation(libs.kotlinx.serialization.json.v161)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    testImplementation(libs.coroutine.test)
    testImplementation(libs.junit)
    testImplementation(libs.core.test)
    testImplementation(libs.truth)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}