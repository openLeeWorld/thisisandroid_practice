plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt") // kotlin버전인 1.9.0과 동일
    // apply false로 잘못 설치된 파일 삭제 후 재빌드 가능
}

android {
    namespace = "kr.co.hanbit.basicsyntax"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "kr.co.hanbit.basicsyntax"
        minSdk = 30
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
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    val dependencyVersion = "1.3.1"
    implementation("androidx.activity:activity-ktx:$dependencyVersion")
    implementation("androidx.fragment:fragment-ktx:$dependencyVersion")

    val fragmentVersion = "1.3.0-beta02"
    // 자바용 fragment 1.3.0
    implementation("androidx.fragment:fragment:$fragmentVersion")
    // 코틀린용 fragment 1.3.0
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")

    val preferenceVersion = "1.1.1"
    implementation ("androidx.preference:preference-ktx:$preferenceVersion")

    val roomVersion = "2.3.0"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.github.bumptech.glide:glide:4.11.0")
    kapt("com.github.bumptech.glide:compiler:4.11.0")
}