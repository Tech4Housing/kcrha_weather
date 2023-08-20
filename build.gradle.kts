plugins {
	java
	application
}

group = "org.kcrha"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

application {
	mainClass.set("org.kcrha.weather.WeatherApplication")
}

tasks.jar {
	manifest {
		attributes(mapOf("Main-Class" to application.mainClass))
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("commons-cli:commons-cli:1.4")
	implementation("org.apache.commons:commons-email:1.5")
	implementation("com.google.code.gson:gson:2.10.1")
	compileOnly("org.projectlombok:lombok:1.18.24")
	annotationProcessor("org.projectlombok:lombok:1.18.24")
	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
