plugins {
    id("com.kastik.library")
    id("com.kastik.hilt")
}

android {
    namespace = "com.kastik.apps.datastore"
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
}