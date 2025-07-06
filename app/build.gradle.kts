import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.gabriel.pocketstatement"
    compileSdk = 35

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.gabriel.pocketstatement"
        minSdk = 24
        targetSdk = 34 // Manter 34 estável
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // --- INÍCIO DA CORREÇÃO ---
        // Carrega o arquivo local.properties de forma explícita
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }

        // Obtém a chave do objeto Properties e fornece um valor padrão se não for encontrada
        val apiKey = localProperties.getProperty("GEMINI_API_KEY", "")

        // Cria o campo no BuildConfig
        buildConfigField("String", "GEMINI_API_KEY", "\"$apiKey\"")
        // --- FIM DA CORREÇÃO ---
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // --- SEÇÃO COMPOSE CORRIGIDA ---
    val composeBom = "2024.05.00"
    // ESTA É A LINHA QUE FALTAVA. Ela define a versão para todas as 'implementation' do Compose abaixo.
    implementation(platform("androidx.compose:compose-bom:$composeBom"))

    // Agora estas linhas sabem que devem usar as versões do BOM 2024.05.00
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Testes também usam o BOM
    androidTestImplementation(platform("androidx.compose:compose-bom:$composeBom"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // --- FIM DA SEÇÃO COMPOSE CORRIGIDA ---

    // Lifecycle & ViewModel for state management
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    // Navigation for moving between screens
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Room for local database (CORRIGINDO A INCONSISTÊNCIA)
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion") // A versão do ksp DEVE ser a mesma

    // Icons
    implementation("androidx.compose.material:material-icons-extended") // Deixe o BOM do Material 2 controlar a versão

    // CameraX
    val cameraXVersion = "1.3.4"
    implementation("androidx.camera:camera-core:${cameraXVersion}")
    implementation("androidx.camera:camera-camera2:${cameraXVersion}")
    implementation("androidx.camera:camera-lifecycle:${cameraXVersion}")
    implementation("androidx.camera:camera-view:${cameraXVersion}")


    // ML Kit for Text Recognition (OCR)
    implementation("com.google.mlkit:text-recognition:16.0.0")

    // Hilt (Dependency Injection)
    implementation("com.google.dagger:hilt-android:2.51")
    ksp("com.google.dagger:hilt-compiler:2.51")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Google AI for Gemini API
    implementation("com.google.ai.client.generativeai:generativeai:0.4.0")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // Coil
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Accompanist for Jetpack Compose Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}