package code.exercise.ce106.orgstructure.csv;

import code.exercise.ce106.orgstructure.model.Employee;

import java.math.BigDecimal;

public class EmployeeCsvRowConverter implements CsvRowConverter<Employee> {

    private static final String DELIMITER = ",";

    private static final int ID_FIELD_IDX         = 0;
    private static final int FIRST_NAME_FIELD_IDX = 1;
    private static final int LAST_NAME_FIELD_IDX  = 2;
    private static final int SALARY_FIELD_IDX     = 3;
    private static final int MANAGER_ID_FIELD_IDX = 4;

    @Override
    public Employee convert(String row) {
        String[] fields = row.split(DELIMITER, -1);
        return new Employee(
            fields[ID_FIELD_IDX],
            fields[FIRST_NAME_FIELD_IDX],
            fields[LAST_NAME_FIELD_IDX],
            new BigDecimal(fields[SALARY_FIELD_IDX]),
            fields[MANAGER_ID_FIELD_IDX].isEmpty() ? null : fields[MANAGER_ID_FIELD_IDX]);
    }

}
