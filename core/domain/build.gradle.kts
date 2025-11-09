plugins {
    id("com.kastik.library")
    id("com.kastik.hilt")
}

android {
    namespace = "com.kastik.apps.domain"
}

dependencies {
    //TODO Find a way to remove this
    implementation(libs.paging.common)
    implementation(project(":core:model"))
}