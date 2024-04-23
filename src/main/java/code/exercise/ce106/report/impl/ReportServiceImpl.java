package code.exercise.ce106.report.impl;

import code.exercise.ce106.orgstructure.OrgStructureFactory;
import code.exercise.ce106.orgstructure.model.OrgStructure;
import code.exercise.ce106.report.ReportService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    private static final Comparator<EmployeeLevel> EMPLOYEE_LEVEL_COMPARATOR =
        Comparator.comparingInt(EmployeeLevel::level).reversed();
    private static final Comparator<ManagerEarnDiff> MANAGER_EARN_DIFF_COMPARATOR =
        Comparator.comparingDouble(ManagerEarnDiff::earnDiffPercent).reversed();

    private static final String INDENT = "  ";

    private final OrgStructureFactory orgStructureFactory;

    public ReportServiceImpl(OrgStructureFactory orgStructureFactory) {
        this.orgStructureFactory = orgStructureFactory;
    }

    @Override
    public void report() {
        var employeeLevels = new ArrayList<EmployeeLevel>();
        var managerEarnLess = new ArrayList<ManagerEarnDiff>();
        var managerEarnMore = new ArrayList<ManagerEarnDiff>();

        var orgStructure = orgStructureFactory.createOrgStructure();
        orgStructureTravers(orgStructure, orgStructure.ceoId(), 0, employeeLevels, managerEarnLess, managerEarnMore);

        printResult(employeeLevels, managerEarnLess, managerEarnMore);
    }

    private void orgStructureTravers(OrgStructure orgStructure, String managerId, int level,
                                     List<EmployeeLevel> employeeLevels, List<ManagerEarnDiff> managerEarnLess,
                                     List<ManagerEarnDiff> managerEarnMore) {
        var employee = orgStructure.employees().get(managerId);

        if (LEVEL_THRESHOLD < level) {
            employeeLevels.add(new EmployeeLevel(employee, level - LEVEL_THRESHOLD));
        }
        
        List<String> subordinates = orgStructure.subordination().get(managerId);
        if (null == subordinates) {
            return;
        }
        
        int nextLevel = level + 1;
        BigDecimal sum = BigDecimal.ZERO;

        for (String id : subordinates) {
            orgStructureTravers(orgStructure, id, nextLevel, employeeLevels, managerEarnLess, managerEarnMore);

            var subordinate = orgStructure.employees().get(id);
            sum = sum.add(subordinate.salary());
        }

        BigDecimal avg = sum.divide(BigDecimal.valueOf(subordinates.size()), SCALE, ROUNDING_MODE);

        BigDecimal atLeastMore = avg.multiply(EARN_AT_LEAST_MORE_FACTOR);
        if (0 > employee.salary().compareTo(atLeastMore)) {
            float diffPercent = employee.salary().divide(atLeastMore, SCALE, ROUNDING_MODE)
                .subtract(BigDecimal.ONE).multiply(HUNDRED).floatValue();
            managerEarnLess.add(new ManagerEarnDiff(employee, Math.abs(diffPercent)));
        } else {
            BigDecimal noMore = avg.multiply(EARN_NO_MORE_FACTOR);
            if (0 < employee.salary().compareTo(noMore)) {
                float diffPercent = employee.salary().divide(noMore, SCALE, ROUNDING_MODE)
                    .subtract(BigDecimal.ONE).multiply(HUNDRED).floatValue();
                managerEarnMore.add(new ManagerEarnDiff(employee, diffPercent));
            }
        }
    }

    private void printResult(List<EmployeeLevel> employeeLevels, List<ManagerEarnDiff> managerEarnLess,
                             List<ManagerEarnDiff> managerEarnMore) {
        System.out.println("Managers earn less than they should:");
        if (managerEarnLess.isEmpty()) {
            printEmptyResultMessage();
        } else {
            managerEarnLess
                .stream()
                .sorted(MANAGER_EARN_DIFF_COMPARATOR)
                .forEach(entry -> System.out.printf("%s%3.0f%% %s %s%n", INDENT, entry.earnDiffPercent(),
                    entry.employee().firstName(), entry.employee().lastName()));
        }

        System.out.println();

        System.out.println("Managers earn more than they should:");
        if (managerEarnMore.isEmpty()) {
            printEmptyResultMessage();
        } else {
            managerEarnMore
                .stream()
                .sorted(MANAGER_EARN_DIFF_COMPARATOR)
                .forEach(entry -> System.out.printf("%s%3.0f%% %s %s%n", INDENT, entry.earnDiffPercent(),
                    entry.employee().firstName(), entry.employee().lastName()));
        }

        System.out.println();

        System.out.println("Employees have a reporting line which is too long:");
        if (employeeLevels.isEmpty()) {
            printEmptyResultMessage();
        } else {
            employeeLevels
                .stream()
                .sorted(EMPLOYEE_LEVEL_COMPARATOR)
                .forEach(entry -> System.out.printf("%s%3d %s %s%n", INDENT, entry.level(),
                    entry.employee().firstName(), entry.employee().lastName()));
        }
    }

    private void printEmptyResultMessage() {
        System.out.printf("%sno one%n", INDENT);
    }
}
