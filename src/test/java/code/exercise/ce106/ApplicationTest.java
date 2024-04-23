package code.exercise.ce106;

import code.exercise.ce106.common.ErrorCode;
import code.exercise.ce106.context.arguments.ArgumentsConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationTest {

    private final PrintStream standardOut = System.out;
    private final PrintStream standardErr = System.err;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errorStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setErr(new PrintStream(errorStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
        System.setErr(standardErr);
    }

    @Test
    void shouldPrintUsageMessageWhenRunWithoutArguments() {
        Application.main(new String[0]);

        assertEquals(0, errorStreamCaptor.size());
        assertEquals("java " + Application.class.getName() + " [--without-header] <CSV file>",
            outputStreamCaptor.toString().trim());
    }

    @Test
    void shouldPrintReportResultWhenRunWithCSVFileArgument() {
        Application.main(new String[] { "./src/test/resources/test.csv" });

        assertEquals(0, errorStreamCaptor.size());

        var output = outputStreamCaptor.toString().trim();

        assertTrue(output.contains("Managers earn less than they should:"));
        assertTrue(output.contains("Managers earn more than they should:"));
        assertTrue(output.contains("Employees have a reporting line which is too long:"));
    }

    @Test
    void shouldPrintErrorMessageWhenRunWithUnexpectedArgument() {
        var argument = ArgumentsConstants.ARG_PREFIX + "unexpected";

        Application.main(new String[] { argument });

        assertEquals(0, outputStreamCaptor.size());

        var output = errorStreamCaptor.toString().trim();

        assertTrue(output.contains(ErrorCode.UNEXPECTED_ARG.getCode()));
        assertTrue(output.contains("Unexpected argument: " + argument));
    }

    @Test
    void shouldPrintErrorMessageWhenRunWithNoExistsCSVFileArgument() {
        var filename = UUID.randomUUID().toString();

        Application.main(new String[] { filename });

        assertEquals(0, outputStreamCaptor.size());

        var output = errorStreamCaptor.toString().trim();

        assertTrue(output.contains(ErrorCode.FILE_NOT_FOUND.getCode()));
        assertTrue(output.contains("File not found: " + filename));
    }

    @Test
    void shouldPrintErrorMessageWhenRunWithHugeCSVFileArgument() {
        var filename = "./src/test/resources/1001.csv";

        Application.main(new String[] { filename });

        assertEquals(0, outputStreamCaptor.size());

        var output = errorStreamCaptor.toString().trim();

        assertTrue(output.contains(ErrorCode.ROWS_LIMIT.getCode()));
        assertTrue(output.contains("Numbers of rows can be up to 1000: " + filename));
    }

    @Test
    void shouldPrintErrorMessageWhenCatchUnexpectedException() throws Exception {
        Application.main(null);

        assertEquals(0, outputStreamCaptor.size());

        var output = errorStreamCaptor.toString().trim();

        assertTrue(output.contains(ErrorCode.UNEXPECTED_EXCEPTION.getCode()));
        assertTrue(output.contains("Unexpected exception:"));
    }

}
