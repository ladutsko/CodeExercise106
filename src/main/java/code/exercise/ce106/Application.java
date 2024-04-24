package code.exercise.ce106;

import code.exercise.ce106.orgstructure.csv.CsvOrgStructureFactory;
import code.exercise.ce106.orgstructure.csv.EmployeeCsvRowConverter;
import code.exercise.ce106.report.console.ConsoleReportOutputService;
import code.exercise.ce106.report.impl.ReportServiceImpl;
import code.exercise.ce106.util.FileUtil;

public final class Application {

    private static final int PARAM_COUNT = 1;

    private static final int CSV_FILENAME_PARAM_IDX = 0;

    private static final String USAGE_MESSAGE = "java " + Application.class.getName() + " <CSV file>%n";

    private Application() {
    }

    public static void main(String[] args) {
        if (PARAM_COUNT != args.length || args[CSV_FILENAME_PARAM_IDX].isBlank()) {
            System.out.printf(USAGE_MESSAGE);
            return;
        }

        var resource = FileUtil.createResource(args[CSV_FILENAME_PARAM_IDX].trim());
        var converter = new EmployeeCsvRowConverter();
        var orgStructureFactory = new CsvOrgStructureFactory(resource, converter);
        var orgStructure = orgStructureFactory.createOrgStructure();

        var reportService = new ReportServiceImpl();
        var reportResult = reportService.report(orgStructure);

        var reportOutputService = new ConsoleReportOutputService();
        reportOutputService.print(reportResult);
    }

}
