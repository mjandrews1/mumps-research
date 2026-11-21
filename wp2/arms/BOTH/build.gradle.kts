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

// Arm BOTH: Prolog rule base (machine-queried, BOTH-local rules_v0.pl
// seeded identical to PRO's v0) + Dafny-verified translation of the
// Mumps II MDH C/C++ corpus. Rule hits constrain the Kotlin form before
// verification begins; verification outcomes feed back into the rule
// base (amendments recorded). Consult events under
// wp2/logs/BOTH_knowledge_events.csv (kind,target,unit,timestamp).
// Grading data (builds, verification outcomes, per-unit checks) is
// recorded in the arm journal and wp2/logs; this project exists so the
// arm translations compile.