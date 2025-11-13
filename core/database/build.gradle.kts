plugins {
    alias(libs.plugins.kastik.library)
    alias(libs.plugins.kastik.hilt)
    alias(libs.plugins.room)
}
android {
    namespace = "com.kastik.apps.database"

    room {
        schemaDirectory("$projectDir/schemas")
    }

}


dependencies {
    testImplementation(project(":core:domain"))
    testImplementation(project(":core:testing"))


    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)


    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.kotlin.test)
}