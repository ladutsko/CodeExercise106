package code.exercise.ce106.context.impl;

import code.exercise.ce106.context.ApplicationContext;
import code.exercise.ce106.context.ApplicationContextFactory;
import code.exercise.ce106.context.arguments.Arguments;
import code.exercise.ce106.orgstructure.csv.CsvOrgStructureFactory;
import code.exercise.ce106.orgstructure.csv.EmployeeCsvRowConverter;
import code.exercise.ce106.report.impl.ReportServiceImpl;

public class ApplicationContextFactoryImpl implements ApplicationContextFactory {

    private final Arguments arguments;

    public ApplicationContextFactoryImpl(Arguments arguments) {
        this.arguments = arguments;
    }

    @Override
    public ApplicationContext createApplicationContext() {
        return new ApplicationContext(createReportService());
    }

    private ReportServiceImpl createReportService() {
        return new ReportServiceImpl(createOrgStructureProvider());
    }

    private CsvOrgStructureFactory createOrgStructureProvider() {
        return new CsvOrgStructureFactory(arguments.filename(), arguments.hasHeader(), createEmployeeCsvRowConverter());
    }

    private EmployeeCsvRowConverter createEmployeeCsvRowConverter() {
        return new EmployeeCsvRowConverter();
    }

}
