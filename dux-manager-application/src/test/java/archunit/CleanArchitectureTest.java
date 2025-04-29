package archunit;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class CleanArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    public static void setup() {
        importedClasses = new ClassFileImporter().importPackages("com.tracktainment.duxmanager");
    }

    @Test
    public void domainShouldNotDependOnOtherLayers() {
        ArchRule rule = noClasses().that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "..dataprovider..",
                        "..controller..",
                        "..api.."
                );

        rule.check(importedClasses);
    }

    @Test
    public void useCasesShouldNotDependOnOuterLayers() {
        ArchRule rule = noClasses().that().resideInAPackage("..usecases..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "..dataprovider.impl..",
                        "..controller..",
                        "..api.."
                );

        rule.check(importedClasses);
    }

    @Test
    public void dataProvidersShouldImplementInterfaces() {
        ArchRule rule = classes().that().haveSimpleNameEndingWith("DataProviderNoSql")
                .should().implement(DescribedPredicate.describe(
                        "interfaces in dataprovider package",
                        JavaClass.Predicates.INTERFACES.and(
                                JavaClass.Predicates.resideInAPackage("..dataprovider..")
                        )
                ));

        rule.check(importedClasses);
    }

    @Test
    public void controllersShouldDependOnUseCases() {
        ArchRule rule = classes().that().resideInAPackage("..controller..")
                .should().dependOnClassesThat().resideInAPackage("..usecases..");

        rule.check(importedClasses);
    }

    @Test
    public void domainShouldNotHaveFrameworkDependencies() {
        ArchRule rule = noClasses().that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "org.springframework..",
                        "jakarta.persistence.."
                );

        rule.check(importedClasses);
    }

    @Test
    public void domainClassesShouldResideInDomainPackage() {
        ArchRule rule = classes().that().haveSimpleNameEndingWith("User")
                .or().haveSimpleNameEndingWith("Asset")
                .and().areNotInterfaces()
                .should().resideInAPackage("..domain..");

        rule.check(importedClasses);
    }

    @Test
    public void useCasesShouldResideInUseCasesPackage() {
        ArchRule rule = classes().that().haveSimpleNameEndingWith("UseCase")
                .should().resideInAPackage("..usecases..");

        rule.check(importedClasses);
    }
}
