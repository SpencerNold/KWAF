import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.9.20"
}

kotlin {
	jvmToolchain {
		languageVersion.set(JavaLanguageVersion.of(8))
	}
}

repositories {
	mavenCentral()
}

dependencies {
	api("com.google.code.gson:gson:2.13.1")

	testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

sourceSets {
	create("examples") {
		kotlin.srcDir("src/examples/kotlin")
		resources.srcDir("src/examples/resources")
		compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
		runtimeClasspath += output + compileClasspath
	}
}

tasks.withType<KotlinCompile>().configureEach {
	compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
}

tasks.test {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
		showStandardStreams = true
	}
}

tasks.register<JavaExec>("runExample") {
	classpath = sourceSets["examples"].runtimeClasspath
	mainClass.set("FirewallExampleKt") // <- change to the entrypoint of the example you want to run
}

tasks.named<Copy>("processExamplesResources") {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}