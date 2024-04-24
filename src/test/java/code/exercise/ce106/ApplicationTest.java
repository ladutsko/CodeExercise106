package code.exercise.ce106;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationTest {

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
    void shouldPrintUsageMessageWhenRunWithoutArguments() {
        Application.main(new String[0]);

        assertEquals("java " + Application.class.getName() + " <CSV file>", outputStreamCaptor.toString().trim());
    }

    @Test
    void shouldPrintUsageMessageWhenRunWithBlankCSVFilename() {
        Application.main(new String[] { " " });

        assertEquals("java " + Application.class.getName() + " <CSV file>", outputStreamCaptor.toString().trim());
    }

    @Test
    void shouldPrintReportResultWhenRunWithCSVFile() {
        Application.main(new String[] { "./src/test/resources/test.csv" });

        var output = outputStreamCaptor.toString().trim();

        assertTrue(output.contains("Managers earn less than they should:"));
        assertTrue(output.contains("Managers earn more than they should:"));
        assertTrue(output.contains("Employees have a reporting line which is too long:"));
    }

}
