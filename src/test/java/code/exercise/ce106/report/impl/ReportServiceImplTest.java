package code.exercise.ce106.report.impl;

import code.exercise.ce106.orgstructure.model.Employee;
import code.exercise.ce106.orgstructure.model.OrgStructure;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReportServiceImplTest {

    private static final float THRESHOLD = 0.00001f;

    private final ReportServiceImpl reportService = new ReportServiceImpl();

    @Test
    void shouldBuildReportResult() {
        var joeDoe = new Employee("123", "Joe", "Doe", new BigDecimal("70000"), null);
        var martinChekov = new Employee("124", "Martin", "Chekov", new BigDecimal("45000"), joeDoe.id());
        var williamAndrews = new Employee("306", "William", "Andrews", new BigDecimal("40000"), "300");
        var chesterRichardson = new Employee("400", "Chester", "Richardson", new BigDecimal("75000"), williamAndrews.id());
        var cherryDavis = new Employee("401", "Cherry", "Davis", new BigDecimal("10000"), chesterRichardson.id());
        var orgStructure = new OrgStructure(
            Map.of(
                joeDoe.id(), joeDoe,
                martinChekov.id(), martinChekov,
                "125", new Employee("125", "Bob", "Ronstad", new BigDecimal("47000"), joeDoe.id()),
                "300", new Employee("300", "Alice", "Hasacat", new BigDecimal("50000"), martinChekov.id()),
                "305", new Employee("305", "Brett", "Hardleaf", new BigDecimal("34000"), "300"),
                williamAndrews.id(), williamAndrews,
                chesterRichardson.id(), chesterRichardson,
                cherryDavis.id(), cherryDavis
            ),
            Map.of(
                joeDoe.id(), List.of(martinChekov.id(), "125"),
                martinChekov.id(), List.of("300"),
                "300", List.of("305", williamAndrews.id()),
                williamAndrews.id(), List.of(chesterRichardson.id()),
                chesterRichardson.id(), List.of(cherryDavis.id())
            ),
            "123"
        );

        var reportResult = reportService.report(orgStructure);

        assertNotNull(reportResult);

        var managersEarnLess = reportResult.managersEarnLess();

        assertNotNull(managersEarnLess);
        assertEquals(2, managersEarnLess.size());
        assertEquals(williamAndrews, managersEarnLess.get(0).employee());
        assertTrue(isFloatingEqual(55.56f, managersEarnLess.get(0).earnDiffPercent()));
        assertEquals(martinChekov, managersEarnLess.get(1).employee());
        assertTrue(isFloatingEqual(25f, managersEarnLess.get(1).earnDiffPercent()));

        var managersEarnMore = reportResult.managersEarnMore();

        assertNotNull(managersEarnMore);
        assertEquals(2, managersEarnMore.size());
        assertEquals(chesterRichardson, managersEarnMore.get(0).employee());
        assertTrue(isFloatingEqual(400f, managersEarnMore.get(0).earnDiffPercent()));
        assertEquals(joeDoe, managersEarnMore.get(1).employee());
        assertTrue(isFloatingEqual(1.45f, managersEarnMore.get(1).earnDiffPercent()));

        var employeesLevel = reportResult.employeesLevel();

        assertNotNull(employeesLevel);
        assertEquals(1, employeesLevel.size());

        var employeeLevel = employeesLevel.get(0);

        assertNotNull(employeeLevel);
        assertEquals(cherryDavis, employeeLevel.employee());
        assertEquals(1, employeeLevel.level());
    }

    @Test
    void shouldBuildEmptyReportResult() {
        var orgStructure = new OrgStructure(
            Map.of(
                "123", new Employee("123", "Joe", "Doe", new BigDecimal("70000"), null)
            ),
            Map.of(),
            "123"
        );

        var reportResult = reportService.report(orgStructure);

        assertNotNull(reportResult);
        assertNotNull(reportResult.employeesLevel());
        assertTrue(reportResult.employeesLevel().isEmpty());
        assertNotNull(reportResult.managersEarnLess());
        assertTrue(reportResult.managersEarnLess().isEmpty());
        assertNotNull(reportResult.managersEarnMore());
        assertTrue(reportResult.managersEarnMore().isEmpty());
    }

    private static boolean isFloatingEqual(float f1, float f2) {
        return THRESHOLD > Math.abs(f1 - f2);
    }

}
