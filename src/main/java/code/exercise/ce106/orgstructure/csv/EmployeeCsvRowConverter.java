package code.exercise.ce106.orgstructure.csv;

import code.exercise.ce106.orgstructure.model.Employee;

import java.math.BigDecimal;
import java.util.function.Function;

/**
 * This implementation converts CSV row string to {@link Employee}
 * A row string represents an employee (CEO included). CEO has no manager specified.
 *
 * <h5>Assumptions</h5>
 * <ul>
 *     <li>cols are in fixed order</li>
 *     <li>Id & managerId fields are string type because there is no enough information about data types</li>
 *     <li>firstName & lastName are without quotes</li>
 *     <li>salary field is positive {@link BigDecimal} type because there is no enough information about the scale</li>
 * </ul>
 */
public class EmployeeCsvRowConverter implements Function<String, Employee> {

    private static final String DELIMITER = ",";
    private static final int RESULT_THRESHOLD = -1;

    private static final int COLS_COUNT = 5;

    private static final int ID_COL_IDX         = 0;
    private static final int FIRST_NAME_COL_IDX = 1;
    private static final int LAST_NAME_COL_IDX  = 2;
    private static final int SALARY_COL_IDX     = 3;
    private static final int MANAGER_ID_COL_IDX = 4;

    private static final String ID_COL_NAME         = "Id";
    private static final String FIRST_NAME_COL_NAME = "firstName";
    private static final String LAST_NAME_COL_NAME  = "lastName";
    private static final String SALARY_COL_NAME     = "salary";

    private static final String UNEXPECTED_COLS_COUNT_FORMAT_STR = "Unexpected cols count: %d";
    private static final String MUST_NOT_BE_BLANK_FORMAT_STR = "%s must not be blank";

    private static final String SALARY_MUST_POSITIVE_EXCEPTION_MSG = SALARY_COL_NAME + " must be positive";

    @Override
    public Employee apply(String line) {
        var cols = line.split(DELIMITER, RESULT_THRESHOLD);
        if (COLS_COUNT != cols.length) {
            throw new IllegalArgumentException(UNEXPECTED_COLS_COUNT_FORMAT_STR.formatted(cols.length));
        }

        validNonBlank(cols[ID_COL_IDX], ID_COL_NAME);
        validNonBlank(cols[FIRST_NAME_COL_IDX], FIRST_NAME_COL_NAME);
        validNonBlank(cols[LAST_NAME_COL_IDX], LAST_NAME_COL_NAME);
        validNonBlank(cols[SALARY_COL_IDX], SALARY_COL_NAME);

        var salary = new BigDecimal(cols[SALARY_COL_IDX].trim());
        if (0 >= salary.compareTo(BigDecimal.ZERO)) {
            throw new IllegalArgumentException(SALARY_MUST_POSITIVE_EXCEPTION_MSG);
        }

        return new Employee(
            cols[ID_COL_IDX].trim(),
            cols[FIRST_NAME_COL_IDX].trim(),
            cols[LAST_NAME_COL_IDX].trim(),
            salary,
            cols[MANAGER_ID_COL_IDX].isBlank() ? null : cols[MANAGER_ID_COL_IDX].trim());
    }

    private void validNonBlank(String value, String name) {
        if (value.isBlank()) {
            throw new IllegalArgumentException(MUST_NOT_BE_BLANK_FORMAT_STR.formatted(name));
        }
    }

}
