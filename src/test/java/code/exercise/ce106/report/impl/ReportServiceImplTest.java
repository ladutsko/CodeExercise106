package code.exercise.ce106.report.impl;

import code.exercise.ce106.orgstructure.OrgStructureFactory;
import code.exercise.ce106.orgstructure.model.Employee;
import code.exercise.ce106.orgstructure.model.OrgStructure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReportServiceImplTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void shouldPrintReportResult() {
        var orgStructureFactory = new OrgStructureFactory() {
            @Override
            public OrgStructure createOrgStructure() {
                return new OrgStructure(
                    Map.of(
                        "123", new Employee("123", "Joe", "Doe", new BigDecimal("70000"), null),
                        "124", new Employee("124", "Martin", "Chekov", new BigDecimal("45000"), "123"),
                        "125", new Employee("125", "Bob", "Ronstad", new BigDecimal("47000"), "123"),
                        "300", new Employee("300", "Alice", "Hasacat", new BigDecimal("50000"), "124"),
                        "305", new Employee("305", "Brett", "Hardleaf", new BigDecimal("34000"), "300"),
                        "306", new Employee("306", "William", "Andrews", new BigDecimal("40000"), "300"),
                        "400", new Employee("400", "Chester", "Richardson", new BigDecimal("75000"), "306"),
                        "401", new Employee("401", "Cherry", "Davis", new BigDecimal("10000"), "400")
                    ),
                    Map.of(
                        "123", List.of("124", "125"),
                        "124", List.of("300"),
                        "300", List.of("305", "306"),
                        "306", List.of("400"),
                        "400", List.of("401")
                    ),
                    "123"
                );
            }
        };
        var reportService = new ReportServiceImpl(orgStructureFactory);

        reportService.report();

        var output = outputStreamCaptor.toString().trim();
        var earnLessIdx = output.indexOf("Managers earn less than they should:");
        var earnMoreIdx = output.indexOf("Managers earn more than they should:");
        var longLineIdx = output.indexOf("Employees have a reporting line which is too long:");

        assertTrue(0 <= earnLessIdx);
        assertTrue(0 <= earnMoreIdx);
        assertTrue(0 <= longLineIdx);

        assertTrue(earnMoreIdx > output.indexOf("25% Martin Chekov", earnLessIdx));
        assertTrue(longLineIdx > output.indexOf("400% Chester Richardson", earnMoreIdx));
        assertTrue(0 < output.indexOf("1 Cherry Davis", longLineIdx));
    }

    @Test
    void shouldPrintNoOne() {
        var orgStructureFactory = new OrgStructureFactory() {
            @Override
            public OrgStructure createOrgStructure() {
                return new OrgStructure(
                    Map.of(
                        "123", new Employee("123", "Joe", "Doe", new BigDecimal("70000"), null)
                    ),
                    Map.of(),
                    "123"
                );
            }
        };
        var reportService = new ReportServiceImpl(orgStructureFactory);

        reportService.report();

        var output = outputStreamCaptor.toString().trim();
        var earnLessIdx = output.indexOf("Managers earn less than they should:");
        var earnMoreIdx = output.indexOf("Managers earn more than they should:");
        var longLineIdx = output.indexOf("Employees have a reporting line which is too long:");

        assertTrue(0 <= earnLessIdx);
        assertTrue(0 <= earnMoreIdx);
        assertTrue(0 <= longLineIdx);

        assertTrue(earnMoreIdx > output.indexOf("no one", earnLessIdx));
        assertTrue(longLineIdx > output.indexOf("no one", earnMoreIdx));
        assertTrue(0 < output.indexOf("no one", longLineIdx));
    }

}
