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

// Arm DAF: Dafny-verified translation of the Mumps II MDH C/C++ corpus.
// Ad-hoc heuristics per the five-arm design: BASE heuristic log available as
// background (consults logged as hits against BASE-log in the arm journal);
// pure decidable units carry a Dafny spec under spec/ verified 0 errors;
// pointer/IO-heavy units are exempt, exemption rate is a measured outcome.
// Grading data (builds, verification outcomes, per-unit checks) is recorded
// in the arm journal and wp2/logs; this project exists so the arm
// translations compile.
