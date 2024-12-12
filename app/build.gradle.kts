plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "vn.btec.campusexpensemanagement"
    compileSdk = 34

    defaultConfig {
<<<<<<< Updated upstream
        applicationId = "vn.btec.campusexpensemanagement"
=======
        applicationId = "com.btec.fpt.campus_expense_manager"
>>>>>>> Stashed changes
        minSdk = 26
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
    buildToolsVersion = "34.0.0"
}

dependencies {
<<<<<<< Updated upstream
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
=======
    implementation(libs.glide)
    implementation(libs.circleimageview)

>>>>>>> Stashed changes
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
<<<<<<< Updated upstream
    implementation("com.github.PhilJay:MPAndroidChart:3.1.0") // Ensure this is correct
    implementation("com.google.android.material:material:1.x.x")
    implementation(libs.navigation.runtime)
=======
    implementation("com.github.PhilJay:MPAndroidChart:MPAndroidChart:3.1.0")
>>>>>>> Stashed changes
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
