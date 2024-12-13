plugins {
    alias(libs.plugins.android.application)
}

android {
<<<<<<< HEAD
    namespace = "vn.btec.campusexpensemanagement"
    compileSdk = 34

    defaultConfig {
        applicationId = "vn.btec.campusexpensemanagement"
        minSdk = 26
=======
    namespace = "vn.btec.campus_expense_management"
    compileSdk = 34

    defaultConfig {
        applicationId = "vn.btec.campus_expense_management"
        minSdk = 29
>>>>>>> lobi_ver
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
<<<<<<< HEAD
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
=======
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
>>>>>>> lobi_ver
    }
}

dependencies {
<<<<<<< HEAD
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
=======

>>>>>>> lobi_ver
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
<<<<<<< HEAD
    implementation("com.github.PhilJay:MPAndroidChart:3.1.0") // Ensure this is correct
    implementation("com.google.android.material:material:1.x.x")
    implementation(libs.navigation.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
=======
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.github.PhilJay:MPAndroidChart:3.1.0")
>>>>>>> lobi_ver
}