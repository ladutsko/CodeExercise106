package code.exercise.ce106.report.model;

import code.exercise.ce106.orgstructure.model.Employee;

public record ManagerEarnDiff(
    Employee employee,
    float earnDiffPercent
) {
}
