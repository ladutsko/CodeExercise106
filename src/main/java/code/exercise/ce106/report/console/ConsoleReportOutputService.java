package code.exercise.ce106.report.console;

import code.exercise.ce106.report.ReportOutputService;
import code.exercise.ce106.report.model.EmployeeLevel;
import code.exercise.ce106.report.model.ManagerEarnDiff;
import code.exercise.ce106.report.model.ReportResult;

import java.util.Comparator;
import java.util.List;

/**
 * This implementation of {@link ReportOutputService} print out {@link ReportResult} to console.

 * <p>The output contains the following:
 * <ul>
 *     <li>which managers earn less than they should, and by how much</li>
 *     <li>which managers earn more than they should, and by how much</li>
 *     <li>which employees have a reporting line which is too long, and by how much</li>
 * </ul>
 *
 * <h5>Assumptions</h5>
 * <ul>
 *     <li>report is in English</li>
 *     <li><code>no one</code> will be printed if some report result is empty</li>
 *     <li>information about managers which earn less/more than should is sorted in descending order by a percentage</li>
 *     <li>percentages are rounded to the nearest whole number</li>
 *     <li>information about employees which reporting line is too long is sorted in descending order by a count</li>
 *     <li>ident is 2 spaces</li>
 *     <li>report results delimiter is new line</li>
 * </ul>
 */
public class ConsoleReportOutputService implements ReportOutputService {

    private static final String MANAGERS_EARN_LESS_THAN_THEY_SHOULD_MESSAGE =
        "Managers earn less than they should:";
    private static final String MANAGERS_EARN_MORE_THAN_THEY_SHOULD_MESSAGE =
        "Managers earn more than they should:";
    private static final String EMPLOYEES_HAVE_REPORTING_LINE_WHICH_IS_TOO_LONG_MESSAGE =
        "Employees have a reporting line which is too long:";

    private static final String INDENT = "  ";

    private static final String MANAGER_EARN_DIFF_FORMAT_STR = INDENT + "%3.0f%% %s %s%n";
    private static final String EMPLOYEE_LEVEL_FORMAT_STR    = INDENT + "%3d %s %s%n";

    private static final String NO_ONE_FORMAT_STR = INDENT + "no one%n";

    private static final Comparator<EmployeeLevel> EMPLOYEE_LEVEL_COMPARATOR =
        Comparator.comparingInt(EmployeeLevel::level).reversed();
    private static final Comparator<ManagerEarnDiff> MANAGER_EARN_DIFF_COMPARATOR =
        Comparator.comparingDouble(ManagerEarnDiff::earnDiffPercent).reversed();

    @Override
    public void print(ReportResult result) {
        printManagersEarn(result.managersEarnLess(), MANAGERS_EARN_LESS_THAN_THEY_SHOULD_MESSAGE);
        System.out.println();
        printManagersEarn(result.managersEarnMore(), MANAGERS_EARN_MORE_THAN_THEY_SHOULD_MESSAGE);
        System.out.println();
        printEmployeesLevel(result.employeesLevel());
    }

    private void printManagersEarn(List<ManagerEarnDiff> managersEarnDiff, String title) {
        System.out.println(title);
        if (managersEarnDiff.isEmpty()) {
            printEmptyResultMessage();
        } else {
            managersEarnDiff
                .stream()
                .sorted(MANAGER_EARN_DIFF_COMPARATOR)
                .forEach(entry -> System.out.printf(MANAGER_EARN_DIFF_FORMAT_STR, entry.earnDiffPercent(),
                    entry.employee().firstName(), entry.employee().lastName()));
        }
    }

    private void printEmployeesLevel(List<EmployeeLevel> employeesLevel) {
        System.out.println(EMPLOYEES_HAVE_REPORTING_LINE_WHICH_IS_TOO_LONG_MESSAGE);
        if (employeesLevel.isEmpty()) {
            printEmptyResultMessage();
        } else {
            employeesLevel
                .stream()
                .sorted(EMPLOYEE_LEVEL_COMPARATOR)
                .forEach(entry -> System.out.printf(EMPLOYEE_LEVEL_FORMAT_STR, entry.level(),
                    entry.employee().firstName(), entry.employee().lastName()));
        }
    }

    private void printEmptyResultMessage() {
        System.out.printf(NO_ONE_FORMAT_STR);
    }

}
