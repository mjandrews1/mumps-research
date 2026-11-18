plugins {
    kotlin("jvm") version "2.1.20"
    application
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

// Arm DOC: cookbook-consulted translation of the Mumps II MDH C/C++ corpus.
// Grading data (builds, per-unit checks) is recorded in the arm journal and
// wp2/logs; this project exists so the arm translations compile.
