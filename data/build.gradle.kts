plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.serialization)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.alican.data"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            type = "String",
            name = "BASE_URL",
            value = "\"https://api.themoviedb.org/3/\""
        )
        buildConfigField(
            type = "String",
            name = "BASE_POSTER_URL",
            value = "\"https://image.tmdb.org/t/p/w500\""
        )
        buildConfigField(
            type = "String",
            name = "API_TOKEN",
            value = "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3OGUzZDUxYjYwYzZiN2E3NzU3N2JkNzNmODI3MTEzOCIsInN1YiI6IjVkZmRmOGEwZDFhODkzMDAxNDg2ZjIzZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.8pncezOjkKsif20QbFwy4GO_1dxOt9Rfdt-EFBQ5EDE\""
        )
        buildConfigField(
            type = "String",
            name = "ROOM_DB_NAME",
            value = "\"movies_db\""
        )
        buildConfigField(
            type = "String",
            name = "CERTIFICATE_PIN",
            value = "\"sha256/7fbf0d5407ac62d759f4e1926caed5b46424488572921dc39ddbae2c824732ee\""
        )
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
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // networking
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)


    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // google auth
    implementation(libs.google.auth)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
}