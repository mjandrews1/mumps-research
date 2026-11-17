plugins {
    kotlin("jvm") version "2.4.21"
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
    id("jacoco")
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

// Static analysis: run explicitly with `gradle --no-daemon detekt`.
// Deliberately NOT wired into `check`/`build`: the arm translates
// vendor C line-for-line, so complexity/style/naming rules that a
// normal Kotlin project would enforce are intentionally moot here
// (detekt.yml disables those rule sets; potential-bugs, exceptions
// and the remaining sets stay at defaults). The compile gate
// (0 errors / 0 warnings) remains the arm's build gate.
detekt {
    buildUponDefaultConfig = true
    config.setFrom(files("detekt.yml"))
}

// Coverage: `gradle --no-daemon jacocoTestReport` (rides `test`;
// no test sources yet - reports land in build/reports/jacoco once
// the B5 finish-line tests exist).
