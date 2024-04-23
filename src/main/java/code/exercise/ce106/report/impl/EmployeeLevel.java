package code.exercise.ce106.report.impl;

import code.exercise.ce106.orgstructure.model.Employee;

public record EmployeeLevel(
    Employee employee,
    int level
) {
}
