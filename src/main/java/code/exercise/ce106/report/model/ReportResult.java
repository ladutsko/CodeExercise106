package code.exercise.ce106.report.model;

import java.util.List;

public record ReportResult(
    List<EmployeeLevel> employeesLevel,
    List<ManagerEarnDiff> managersEarnLess,
    List<ManagerEarnDiff> managersEarnMore
) {

    public ReportResult(List<EmployeeLevel> employeesLevel, List<ManagerEarnDiff> managersEarnLess,
                        List<ManagerEarnDiff> managersEarnMore) {
        this.employeesLevel = List.copyOf(employeesLevel);
        this.managersEarnLess = List.copyOf(managersEarnLess);
        this.managersEarnMore = List.copyOf(managersEarnMore);
    }

}
