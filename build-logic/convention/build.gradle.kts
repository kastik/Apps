plugins {
    `kotlin-dsl`
}

group = "com.kastik.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        create("androidApplication") {
            id = "com.kastik.application"
            implementationClass = "com.kastik.apps.application.AndroidApplicationConventionPlugin"
        }

        create("androidApplicationCompose") {
            id = "com.kastik.application.compose"
            implementationClass =
                "com.kastik.apps.application.AndroidApplicationComposeConventionPlugin"
        }

        create("androidLibrary") {
            id = "com.kastik.library"
            implementationClass = "com.kastik.apps.library.AndroidLibraryConventionPlugin"
        }

        create("androidLibraryCompose") {
            id = "com.kastik.library.compose"
            implementationClass = "com.kastik.apps.library.AndroidLibraryComposeConventionPlugin"
        }

        create("hilt") {
            id = "com.kastik.hilt"
            implementationClass = "com.kastik.apps.hilt.AndroidHiltConventionPlugin"
        }
    }
}