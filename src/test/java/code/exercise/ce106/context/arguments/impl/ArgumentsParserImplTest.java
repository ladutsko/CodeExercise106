package code.exercise.ce106.context.arguments.impl;

import code.exercise.ce106.common.ApplicationException;
import code.exercise.ce106.common.ErrorCode;
import code.exercise.ce106.context.arguments.ArgumentsConstants;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentsParserImplTest {

    private final ArgumentsParserImpl argumentsParser = new ArgumentsParserImpl();

    @Test
    void shouldParseSuccessfulWhenNoArguments() {
        var arguments = argumentsParser.parse(new String[0]);

        assertNotNull(arguments);
        assertNull(arguments.filename());
        assertEquals(ArgumentsConstants.DEFAULT_HAS_HEADER_VAL, arguments.hasHeader());
    }

    @Test
    void shouldParseSuccessfulWhenOnlyFilenameArgument() {
        var filename = UUID.randomUUID().toString();

        var arguments = argumentsParser.parse(new String[] { filename });

        assertNotNull(arguments);
        assertEquals(filename, arguments.filename());
        assertEquals(ArgumentsConstants.DEFAULT_HAS_HEADER_VAL, arguments.hasHeader());
    }

    @Test
    void shouldParseSuccessfulWhenHeaderFlagAndFilenameArguments() {
        var filename = UUID.randomUUID().toString();

        var arguments = argumentsParser.parse(new String[] { ArgumentsConstants.WITHOUT_HEADER_ARG, filename });

        assertNotNull(arguments);
        assertEquals(filename, arguments.filename());
        assertFalse(arguments.hasHeader());
    }

    @Test
    void shouldThrowExceptionWhenOneMoreFilenameArguments() {
        var filename1 = UUID.randomUUID().toString();
        var filename2 = UUID.randomUUID().toString();

        var e = assertThrowsExactly(ApplicationException.class,
                () -> argumentsParser.parse(new String[] { filename1, filename2 }));

        assertEquals(ErrorCode.UNEXPECTED_ARG, e.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenUnexpectedArgument() {
        var e = assertThrowsExactly(ApplicationException.class,
                () -> argumentsParser.parse(new String[] { ArgumentsConstants.ARG_PREFIX + "unexpected" }));

        assertEquals(ErrorCode.UNEXPECTED_ARG, e.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenWrongArgumentsOrder() {
        var e = assertThrowsExactly(ApplicationException.class,
            () -> argumentsParser.parse(new String[] { UUID.randomUUID().toString(),
                ArgumentsConstants.WITHOUT_HEADER_ARG }));

        assertEquals(ErrorCode.UNEXPECTED_ARG, e.getErrorCode());
    }
}
