package code.exercise.ce106.report.impl;

import code.exercise.ce106.orgstructure.model.OrgStructure;
import code.exercise.ce106.report.ReportService;
import code.exercise.ce106.report.model.EmployeeLevel;
import code.exercise.ce106.report.model.ManagerEarnDiff;
import code.exercise.ce106.report.model.ReportResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * This implementation of {@link ReportService} analyzes {@link OrgStructure} and identifies potential improvements.

 * <p>According Board requirements every manager earns should be at least 20% more than the average salary of its
 * direct subordinates, but no more than 50% more than that average.

 * <p>Also it identifies all employees which have more than 4 managers between them and the CEO.
 *
 * <p>{@link ReportResult} contains the following:
 * <ul>
 *     <li>which managers earn less than they should, and by how much</li>
 *     <li>which managers earn more than they should, and by how much</li>
 *     <li>which employees have a reporting line which is too long, and by how much</li>
 * </ul>
 *
 * <h5>Assumptions</h5>
 * <ul>
 *     <li>use recursion to traverse org structure because default stack size allows it. At worst, it is 1000 calls</li>
 *     <li>information about managers which earn less/more than should contains a diff in percentage of how much they
 * should and manager full name</li>
*      <li>information about employees which reporting line is too long contains a count of extra levels and
 * employee full name</li>
 *     <li>scale is 4</li>
 *     <li>rounding mode is half up</li>
 * </ul>
 */
public class ReportServiceImpl implements ReportService {

    private static final float EARN_AT_LEAST_MORE_PERCENT_THRESHOLD = 20f;
    private static final float EARN_NO_MORE_PERCENT_THRESHOLD       = 50f;

    private static final int LEVEL_THRESHOLD = 4;

    private static final int SCALE = 4;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private static final BigDecimal EARN_AT_LEAST_MORE_FACTOR = BigDecimal.ONE.add(
        BigDecimal.valueOf(EARN_AT_LEAST_MORE_PERCENT_THRESHOLD).divide(HUNDRED, SCALE, ROUNDING_MODE));
    private static final BigDecimal EARN_NO_MORE_FACTOR = BigDecimal.ONE.add(
        BigDecimal.valueOf(EARN_NO_MORE_PERCENT_THRESHOLD).divide(HUNDRED, SCALE, ROUNDING_MODE));

    @Override
    public ReportResult report(OrgStructure orgStructure) {
        var employeesLevel = new ArrayList<EmployeeLevel>();
        var managersEarnLess = new ArrayList<ManagerEarnDiff>();
        var managersEarnMore = new ArrayList<ManagerEarnDiff>();

        orgStructureTravers(orgStructure, orgStructure.ceoId(), 0, employeesLevel, managersEarnLess, managersEarnMore);

        return new ReportResult(employeesLevel, managersEarnLess, managersEarnMore);
    }

    private void orgStructureTravers(OrgStructure orgStructure, String managerId, int level,
                                     List<EmployeeLevel> employeesLevel, List<ManagerEarnDiff> managersEarnLess,
                                     List<ManagerEarnDiff> managersEarnMore) {
        var employee = orgStructure.employees().get(managerId);

        if (LEVEL_THRESHOLD < level) {
            employeesLevel.add(new EmployeeLevel(employee, level - LEVEL_THRESHOLD));
        }
        
        var subordinates = orgStructure.subordination().get(managerId);
        if (null == subordinates) {
            return;
        }
        
        var nextLevel = level + 1;
        var sum = BigDecimal.ZERO;

        for (var id : subordinates) {
            orgStructureTravers(orgStructure, id, nextLevel, employeesLevel, managersEarnLess, managersEarnMore);

            var subordinate = orgStructure.employees().get(id);
            sum = sum.add(subordinate.salary());
        }

        var avg = sum.divide(BigDecimal.valueOf(subordinates.size()), SCALE, ROUNDING_MODE);

        var atLeastMore = avg.multiply(EARN_AT_LEAST_MORE_FACTOR);
        if (0 > employee.salary().compareTo(atLeastMore)) {
            var diffPercent = employee.salary().divide(atLeastMore, SCALE, ROUNDING_MODE)
                .subtract(BigDecimal.ONE).multiply(HUNDRED).floatValue();
            managersEarnLess.add(new ManagerEarnDiff(employee, Math.abs(diffPercent)));
        } else {
            var noMore = avg.multiply(EARN_NO_MORE_FACTOR);
            if (0 < employee.salary().compareTo(noMore)) {
                var diffPercent = employee.salary().divide(noMore, SCALE, ROUNDING_MODE)
                    .subtract(BigDecimal.ONE).multiply(HUNDRED).floatValue();
                managersEarnMore.add(new ManagerEarnDiff(employee, diffPercent));
            }
        }
    }

}
