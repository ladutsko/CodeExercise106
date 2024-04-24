package code.exercise.ce106.orgstructure.csv;

import code.exercise.ce106.orgstructure.OrgStructureFactory;
import code.exercise.ce106.orgstructure.model.Employee;
import code.exercise.ce106.orgstructure.model.OrgStructure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This implementation of {@link OrgStructureFactory} creates {@link OrgStructure} from CSV file.
 * File structure looks like this:
 * <pre>
 *     Id,firstName,lastName,salary,managerId
 *     123,Joe,Doe,60000,
 *     124,Martin,Chekov,45000,123
 *     125,Bob,Ronstad,47000,123
 *     300,Alice,Hasacat,50000,124
 *     305,Brett,Hardleaf,34000,300
 * </pre>
 * Each line represents an employee (CEO included). CEO has no manager specified. Number of rows can be up to 1000.
 *
 * <h5>Assumptions</h5>
 * <ul>
 *     <li>number of rows include a header</li>
 * </ul>
 */
public class CsvOrgStructureFactory implements OrgStructureFactory {

    private static final int ROWS_LIMIT = 1000;

    private static final String ROWS_LIMIT_EXCEPTION_MSG = "Number of rows can be up to " + ROWS_LIMIT;
    private static final String CEO_IS_UNDEFINED = "CEO is undefined";

    private static final String EMPLOYEE_ALREADY_EXISTS_FORMAT_STR = "Employee with Id '%s' already exists";
    private static final String CEO_ALREADY_DEFINED_FORMAT_STR = "CEO with Id '%s' already defined";

    private final Supplier<Reader> resource;
    private final Function<String, Employee> converter;

    public CsvOrgStructureFactory(Supplier<Reader> resource, Function<String, Employee> converter) {
        this.resource = resource;
        this.converter = converter;
    }

    @Override
    public OrgStructure createOrgStructure() {
        try (var reader = new LineNumberReader(resource.get())) {
            var employees = new HashMap<String, Employee>();
            var subordination = new HashMap<String, List<String>>();
            String ceoId = null;

            skipHeader(reader);

            for (var line = reader.readLine(); null != line; line = reader.readLine()) {
                if (ROWS_LIMIT < reader.getLineNumber()) {
                    throw new IOException(ROWS_LIMIT_EXCEPTION_MSG);
                }

                if (line.isBlank()) {
                    continue;
                }

                var employee = converter.apply(line);
                if (null != employees.putIfAbsent(employee.id(), employee)) {
                    throw new IllegalStateException(EMPLOYEE_ALREADY_EXISTS_FORMAT_STR.formatted(employee.id()));
                }

                if (null == employee.managerId()) {
                    if (null != ceoId) {
                        throw new IllegalStateException(CEO_ALREADY_DEFINED_FORMAT_STR.formatted(ceoId));
                    }

                    ceoId = employee.id();
                } else {
                    subordination
                        .computeIfAbsent(employee.managerId(), key -> new ArrayList<>())
                        .add(employee.id());
                }
            }

            if (null == ceoId) {
                throw new IllegalStateException(CEO_IS_UNDEFINED);
            }

            return new OrgStructure(employees, subordination, ceoId);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void skipHeader(BufferedReader reader) throws IOException {
        reader.readLine();
    }

}
