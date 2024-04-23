package code.exercise.ce106.context.impl;

import code.exercise.ce106.context.arguments.Arguments;
import code.exercise.ce106.context.arguments.ArgumentsConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextFactoryImplTest {

    private final Arguments arguments =
        new Arguments("./src/test/resources/test.csv", ArgumentsConstants.DEFAULT_HAS_HEADER_VAL);
    private final ApplicationContextFactoryImpl contextFactory = new ApplicationContextFactoryImpl(arguments);

    @Test
    void shouldCreateContext() {
        var context = contextFactory.createApplicationContext();

        assertNotNull(context);
        assertNotNull(context.reportService());
    }

}
