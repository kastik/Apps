plugins {
    id("com.kastik.library")
    id("com.kastik.hilt")
}
android {
    namespace = "com.kastik.apps.database"
}


dependencies {
    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
}