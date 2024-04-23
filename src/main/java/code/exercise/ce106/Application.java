package code.exercise.ce106;

import code.exercise.ce106.common.ApplicationException;
import code.exercise.ce106.common.ErrorCode;
import code.exercise.ce106.context.arguments.ArgumentsConstants;
import code.exercise.ce106.context.arguments.impl.ArgumentsParserImpl;
import code.exercise.ce106.context.impl.ApplicationContextFactoryImpl;

public final class Application {

    private Application() {
        // There are no use cases for this class where you need to build an object. You can only use static items.
        // I am preventing you from even trying to build an object of this class.
    }

    public static void main(String[] args) {
        try {
            var argumentParser = new ArgumentsParserImpl();
            var arguments = argumentParser.parse(args);
            if (null == arguments.filename()) {
                System.out.printf("java %s [%s] <CSV file>%n", Application.class.getName(),
                    ArgumentsConstants.WITHOUT_HEADER_ARG);
                return;
            }

            var contextBuilder = new ApplicationContextFactoryImpl(arguments);
            var context = contextBuilder.createApplicationContext();
            var reportService = context.reportService();
            reportService.report();
        } catch (Exception e) {
            ApplicationException ae;
            if (e instanceof ApplicationException aex) {
                ae = aex;
            } else {
                ae = new ApplicationException(ErrorCode.UNEXPECTED_EXCEPTION, e, e.getMessage());
            }
            ae.printStackTrace();
        }
    }

}
