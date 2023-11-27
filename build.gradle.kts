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
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("commons-cli:commons-cli:1.4")
	implementation("commons-io:commons-io:2.13.0")
	implementation("org.apache.commons:commons-text:1.10.0")
	implementation("org.jsoup:jsoup:1.16.2")
	implementation("org.simplejavamail:simple-java-mail:8.2.0")
	implementation("org.xhtmlrenderer:flying-saucer-pdf-openpdf:9.3.1")
	compileOnly("org.projectlombok:lombok:1.18.24")
	annotationProcessor("org.projectlombok:lombok:1.18.24")
	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
