plugins {
    id("com.kastik.application.compose")
    id("com.kastik.hilt")
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.gms)
}

android {
    namespace = "com.kastik.apps"

    defaultConfig {
        applicationId = "com.kastik.apps"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":feature:home"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:announcement"))
    implementation(project(":feature:settings"))

    // TODO Remove this once log module is created
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

}