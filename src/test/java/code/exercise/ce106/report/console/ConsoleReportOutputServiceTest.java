package code.exercise.ce106.report.console;

import code.exercise.ce106.orgstructure.model.Employee;
import code.exercise.ce106.report.model.EmployeeLevel;
import code.exercise.ce106.report.model.ManagerEarnDiff;
import code.exercise.ce106.report.model.ReportResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleReportOutputServiceTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private final ConsoleReportOutputService reportOutputService = new ConsoleReportOutputService();

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
        var joeDoe = new Employee("123", "Joe", "Doe", new BigDecimal("70000"), null);
        var martinChekov = new Employee("124", "Martin", "Chekov", new BigDecimal("45000"), joeDoe.id());
        var williamAndrews = new Employee("306", "William", "Andrews", new BigDecimal("40000"), martinChekov.id());
        var reportResult = new ReportResult(
            List.of(new EmployeeLevel(williamAndrews, 1)),
            List.of(new ManagerEarnDiff(martinChekov, 10.1f)),
            List.of(new ManagerEarnDiff(joeDoe, 19.95f)));

        reportOutputService.print(reportResult);

        var output = outputStreamCaptor.toString().trim();
        var earnLessIdx = output.indexOf("Managers earn less than they should:");
        var earnMoreIdx = output.indexOf("Managers earn more than they should:");
        var longLineIdx = output.indexOf("Employees have a reporting line which is too long:");

        assertTrue(0 <= earnLessIdx);
        assertTrue(0 <= earnMoreIdx);
        assertTrue(0 <= longLineIdx);

        assertTrue(earnMoreIdx > output.indexOf("10% Martin Chekov", earnLessIdx));
        assertTrue(longLineIdx > output.indexOf("20% Joe Doe", earnMoreIdx));
        assertTrue(0 < output.indexOf("1 William Andrews", longLineIdx));
    }

    @Test
    void shouldPrintNoOneWhenEmptyResult() {
        var reportResult = new ReportResult(List.of(), List.of(), List.of());

        reportOutputService.print(reportResult);

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
