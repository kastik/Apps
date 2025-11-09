plugins {
    id("com.kastik.library.compose")
    id("com.kastik.hilt")
}

android {
    namespace = "com.kastik.apps.home"
}
dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
}