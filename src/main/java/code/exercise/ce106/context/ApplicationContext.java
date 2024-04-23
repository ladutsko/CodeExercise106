package code.exercise.ce106.context;

import code.exercise.ce106.report.ReportService;

public record ApplicationContext(
    ReportService reportService
) {
}
