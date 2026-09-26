import com.android.build.api.component.analytics.AnalyticsEnabledApplicationVariant
import com.android.build.api.variant.impl.ApplicationVariantImpl
import java.nio.file.Paths

plugins {
  id("com.android.application")
  kotlin("android")
  id("com.google.devtools.ksp")
  id("kotlin-parcelize")
  id("kotlinx-serialization")
  //id("dev.rikka.tools.materialthemebuilder")
}

val verName = "2.5.5b"
val verCode = 2050502

android {
  compileSdk = 34
  ndkVersion = "25.0.8775105"

  defaultConfig {
    applicationId = "com.absinthe.anywhere_"
    namespace = "com.absinthe.anywhere_"
    minSdk = 27
    targetSdk = 33
    versionCode = verCode
    versionName = verName
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    manifestPlaceholders["appName"] = "Anywhere-"
    ndk {
      //noinspection ChromeOsAbiSupport
      abiFilters += "arm64-v8a"
    }
    resourceConfigurations += "en"
  }

  ksp {
    arg("room.incremental", "true")
    arg("room.schemaLocation", "$projectDir/schemas")
  }

  buildFeatures {
    aidl = false
    buildConfig = true
    viewBinding = true
  }

  buildTypes {
    debug {
      applicationIdSuffix = ".debug"
      manifestPlaceholders["appName"] = "Anywhere-β"
      buildConfigField("boolean", "BETA", "true")
    }
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
      buildConfigField("boolean", "BETA", "false")
    }
    all {
      buildConfigField(
        "String",
        "APP_CENTER_SECRET",
        "\"" + System.getenv("APP_CENTER_SECRET").orEmpty() + "\""
      )
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  androidComponents.onVariants { v ->
    val variant: ApplicationVariantImpl =
      if (v is ApplicationVariantImpl) v
      else (v as AnalyticsEnabledApplicationVariant).delegate as ApplicationVariantImpl
    variant.outputs.forEach {
      it.outputFileName.set("Anywhere-${verName}-${verCode}-${variant.name}.apk")
    }
  }

  dependenciesInfo.includeInApk = false


  packaging {
    resources {
      excludes += "META-INF/**"
      excludes += "okhttp3/**"
      excludes += "kotlin/**"
      excludes += "org/**"
      excludes += "**.properties"
      excludes += "**.bin"
    }
  }
}

/*materialThemeBuilder {
  themes {
    create("anywhere") {
      primaryColor = "#8BC34A"
      lightThemeFormat = "Theme.Material3.Light.%s"
      lightThemeParent = "Theme.Material3.Light.Rikka"
      darkThemeFormat = "Theme.Material3.Dark.%s"
      darkThemeParent = "Theme.Material3.Dark.Rikka"
    }
  }
  generatePalette = true
}*/

repositories {
  mavenCentral()
}

val optimizeReleaseRes: Task = task("optimizeReleaseRes").doLast {
  val aapt2 = File(
    androidComponents.sdkComponents.sdkDirectory.get().asFile,
    "build-tools/${project.android.buildToolsVersion}/aapt2"
  )
  val zip = Paths.get(
    buildDir.path,
    "intermediates",
    "optimized_processed_res",
    "release",
    "resources-release-optimize.ap_"
  )
  val optimized = File("${zip}.opt")
  val cmd = exec {
    commandLine(
      aapt2, "optimize",
      "--collapse-resource-names",
      "--resources-config-path", "aapt2-resources.cfg",
      "-o", optimized,
      zip
    )
    isIgnoreExitValue = false
  }
  if (cmd.exitValue == 0) {
    delete(zip)
    optimized.renameTo(zip.toFile())
  }
}

tasks.configureEach {
  if (name == "optimizeReleaseResources") {
    finalizedBy(optimizeReleaseRes)
  }
}

configurations.all {
  exclude(group = "androidx.appcompat", module = "appcompat")
  exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk7")
  exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
}

dependencies {
  implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

  //implementation(project(":color-picker"))
  //implementation(files("libs/IceBox-SDK-1.0.6.aar"))

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

  //implementation("com.github.zhaobozhen.libraries:me:1.1.4")
  //implementation("com.github.zhaobozhen.libraries:utils:1.1.4")

  //implementation("com.microsoft.appcenter:appcenter-analytics:${appCenterSdkVersion}")
  //implementation("com.microsoft.appcenter:appcenter-crashes:${appCenterSdkVersion}")

  //Android X
  val roomVersion = "2.5.2"
  implementation("androidx.room:room-runtime:${roomVersion}")
  implementation("androidx.room:room-ktx:${roomVersion}")
  ksp("androidx.room:room-compiler:${roomVersion}")
  androidTestImplementation("androidx.room:room-testing:${roomVersion}")

  val lifecycleVersion = "2.6.2"
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:${lifecycleVersion}")
  implementation("androidx.lifecycle:lifecycle-common-java8:${lifecycleVersion}")
  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${lifecycleVersion}")
  implementation("androidx.recyclerview:recyclerview:1.3.1")


  //KTX
  implementation("androidx.preference:preference-ktx:1.2.1")

  //implementation("com.google.code.gson:gson:2.9.0")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
  implementation("com.blankj:utilcodex:1.31.1")
  //implementation("com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.11")


  val shizukuVersion = "12.2.0"
  // required by Shizuku and Sui
  implementation("dev.rikka.shizuku:api:$shizukuVersion")
  // required by Shizuku
  implementation("dev.rikka.shizuku:provider:$shizukuVersion")

  implementation("dev.rikka.rikkax.material:material-preference:2.0.0")

}
