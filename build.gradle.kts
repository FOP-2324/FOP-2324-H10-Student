plugins {
    alias(libs.plugins.algomate)
    alias(libs.plugins.jagr.gradle)
}

exercise {
    assignmentId.set("h10")
}

submission {
    // ACHTUNG!
    // Setzen Sie im folgenden Bereich Ihre TU-ID (NICHT Ihre Matrikelnummer!), Ihren Nachnamen und Ihren Vornamen
    // in Anführungszeichen (z.B. "ab12cdef" für Ihre TU-ID) ein!
    studentId = null
    firstName = null
    lastName = null

    // Optionally require own tests for mainBuildSubmission task. Default is false
    requireTests = false
}

dependencies {
    implementation(libs.algoutils.student)
}

jagr {
    graders {
        val graderPublic by getting {
            graderName.set("H10-Public")
            rubricProviderName.set("h10.H10_RubricProviderPublic")
            configureDependencies {
                implementation(libs.algoutils.tutor)
                implementation(libs.junit.pioneer)
            }
        }
    }
}
