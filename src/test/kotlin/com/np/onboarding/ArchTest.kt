package com.np.onboarding

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.Test

class ArchTest {

    @Test
    fun servicesAndRepositoriesShouldNotDependOnWebLayer() {

        val importedClasses = ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.np.onboarding")

        noClasses()
            .that()
                .resideInAnyPackage("com.np.onboarding.service..")
            .or()
                .resideInAnyPackage("com.np.onboarding.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.np.onboarding.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses)
    }
}
