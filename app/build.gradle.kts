import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = libs.versions.applicationId.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.applicationVersionCode.get().toInt()
        versionName = libs.versions.applicationVersionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    signingConfigs {
        create("release") {
            storeFile = rootProject.file(providers.gradleProperty("RELEASE_STORE_FILE"))
            storePassword = providers.gradleProperty("RELEASE_STORE_PASSWORD").get()
            keyAlias = providers.gradleProperty("RELEASE_KEY_ALIAS").get()
            keyPassword = providers.gradleProperty("RELEASE_KEY_PASSWORD").get()

            setProperty(
                "archivesBaseName",
                "Movies-v${libs.versions.applicationVersionName.get()}-${libs.versions.applicationVersionCode.get()}"
            )
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {

        }
    }
    flavorDimensions += "default"

    productFlavors {
        create("prod") {
            dimension = "default"
        }
        create("dev") {
            dimension = "default"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {


    implementation(project(":domain"))
    implementation(project(":data"))


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.hilt.navigation.compose)



    // Coil ImageLoader
    implementation(libs.coil.compose)


    implementation(libs.kotlinx.serialization.json)



    // Coroutine and Lifecycle
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)



    implementation(libs.androidx.material.icons.core)
    // Optional - Add full set of material icons
    implementation(libs.androidx.material.icons.extended)
    // Optional - Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.androidx.navigation.compose)

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.auth)

    //google auth
    implementation(libs.google.auth)

    //datastore
    implementation(libs.androidx.datastore.preferences)


    // navigation
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)

    // material 3
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.compose.adaptive.navigation3)


}