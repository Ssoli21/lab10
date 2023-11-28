plugins {
    application
    java
    id("org.danilopianini.gradle-java-qa") version "1.25.0"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.7.3")
}
application {
    mainClass.set("it.unibo.mvc.DrawNumberApp")
}
