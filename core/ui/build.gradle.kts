plugins {
    alias(libs.plugins.kastik.library.compose)
    alias(libs.plugins.kastik.hilt)
}

android {
    namespace = "com.kastik.apps.core.ui"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:analytics"))
    implementation(project(":core:designsystem"))
    implementation(libs.paging.compose)
}