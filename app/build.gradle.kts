import com.android.build.api.variant.ComponentIdentity
import sp.gx.core.camelCase
import sp.gx.core.kebabCase

repositories {
    google()
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

plugins {
    id("com.android.application")
    id("kotlin-android")
}

fun ComponentIdentity.getVersion(): String {
    val flavors = productFlavors.map { (it, _) -> it }
    check(flavors.isEmpty()) { "Flavors \"$flavorName\" are not supported!" }
    val versionName = android.defaultConfig.versionName ?: error("No version name!")
    check(versionName.isNotBlank())
    val versionCode = android.defaultConfig.versionCode ?: error("No version code!")
    check(versionCode > 0)
    check(name.isNotBlank())
    return when (buildType) {
        "debug", "examine" -> kebabCase(
            versionName,
            name,
            versionCode.toString(),
        )
        "release" -> kebabCase(
            versionName,
            versionCode.toString(),
        )
        else -> error("Build type \"${buildType}\" is not supported!")
    }
}

android {
    namespace = "org.kepocnhh.thewolf"
    compileSdk = Version.Android.compileSdk

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                // https://stackoverflow.com/a/71834475/4398606
                it.configure<JacocoTaskExtension> {
                    isIncludeNoLocationClasses = true
                    excludes = listOf("jdk.internal.*")
                }
            }
        }
    }

    defaultConfig {
        applicationId = namespace
        minSdk = Version.Android.minSdk
        targetSdk = Version.Android.targetSdk
        versionCode = 4
        versionName = "0.0.$versionCode"
        manifestPlaceholders["appName"] = "@string/app_name"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".$name"
            versionNameSuffix = "-$name"
            isMinifyEnabled = false
            isShrinkResources = false
            manifestPlaceholders["buildType"] = name
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions.kotlinCompilerExtensionVersion = Version.Android.compose
}

androidComponents.onVariants { variant ->
    val output = variant.outputs.single()
    check(output is com.android.build.api.variant.impl.VariantOutputImpl)
    output.outputFileName = "${kebabCase(rootProject.name, variant.getVersion())}.apk"
    afterEvaluate {
        tasks.getByName<JavaCompile>(camelCase("compile", variant.name, "JavaWithJavac")) {
            targetCompatibility = Version.jvmTarget
        }
        tasks.getByName<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>(camelCase("compile", variant.name, "Kotlin")) {
            kotlinOptions.jvmTarget = Version.jvmTarget
        }
        if (variant.buildType == android.testBuildType) {
            tasks.getByName<JavaCompile>(camelCase("compile", variant.name, "UnitTest", "JavaWithJavac")) {
                targetCompatibility = Version.jvmTarget
            }
            tasks.getByName<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>(camelCase("compile", variant.name, "UnitTest", "Kotlin")) {
                kotlinOptions.jvmTarget = Version.jvmTarget
            }
            // todo checkCoverage
            // todo checkCodeQuality
        } else {
            // todo checkCodeQuality
        }
        // todo checkReadme
        // todo checkCodeStyle
        // todo assembleMetadata
        val checkManifestTask = task(camelCase("checkManifest", variant.name)) {
            dependsOn(camelCase("compile", variant.name, "Sources"))
            doLast {
                val file = "intermediates/merged_manifest/${variant.name}/AndroidManifest.xml"
                val manifest = groovy.xml.XmlParser().parse(layout.buildDirectory.file(file).get().asFile)
                val actual = manifest.getAt(groovy.namespace.QName("uses-permission")).map {
                    check(it is groovy.util.Node)
                    val attributes = it.attributes().mapKeys { (k, _) -> k.toString() }
                    val name = attributes["{http://schemas.android.com/apk/res/android}name"]
                    check(name is String && name.isNotEmpty())
                    name
                }
                val applicationId by variant.applicationId
                val expected = setOf(
                    "$applicationId.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION",
                )
                check(actual.sorted() == expected.sorted()) {
                    "Actual is:\n$actual\nbut expected is:\n$expected"
                }
            }
        }
        tasks.getByName(camelCase("assemble", variant.name)) {
            dependsOn(checkManifestTask)
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.compose.foundation:foundation:${Version.Android.compose}")
    implementation("com.github.kepocnhh:Logics:0.1.3-SNAPSHOT")
    implementation("com.github.kepocnhh:ComposeAnimations:0.0.2-SNAPSHOT")
    implementation("com.github.kepocnhh:ComposeKeyboard:0.0.1u-SNAPSHOT")
    implementation("com.github.kepocnhh:ComposeSquares:0.0.1u-SNAPSHOT")
}
