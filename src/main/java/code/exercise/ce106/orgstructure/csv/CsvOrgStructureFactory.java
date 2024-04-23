package code.exercise.ce106.orgstructure.csv;

import code.exercise.ce106.common.ApplicationException;
import code.exercise.ce106.common.ErrorCode;
import code.exercise.ce106.orgstructure.OrgStructureFactory;
import code.exercise.ce106.orgstructure.model.Employee;
import code.exercise.ce106.orgstructure.model.OrgStructure;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CsvOrgStructureFactory implements OrgStructureFactory {

    private static final int ROWS_LIMIT = 1000;

    private final String filename;
    private final boolean hasHeader;
    private final CsvRowConverter<Employee> converter;

    public CsvOrgStructureFactory(String filename, boolean hasHeader, CsvRowConverter<Employee> converter) {
        this.filename = filename;
        this.hasHeader = hasHeader;
        this.converter = converter;
    }

    @Override
    public OrgStructure createOrgStructure() {
        var employees = new HashMap<String, Employee>();
        var subordination = new HashMap<String, List<String>>();
        String ceoId = null;

        try (var reader = new LineNumberReader(new FileReader(filename, Charset.defaultCharset()))) {
            try {
                String row;

                if (hasHeader) {
                    // Skip header
                    row = reader.readLine();
                }

                row = reader.readLine();
                while (null != row) {
                    if (ROWS_LIMIT < reader.getLineNumber()) {
                        throw new ApplicationException(ErrorCode.ROWS_LIMIT, ROWS_LIMIT, filename);
                    }

                    var employee = converter.convert(row);
                    employees.put(employee.id(), employee);

                    if (null == employee.managerId()) {
                        ceoId = employee.id();
                    } else {
                        subordination
                            .computeIfAbsent(employee.managerId(), key -> new ArrayList<>())
                            .add(employee.id());
                    }

                    row = reader.readLine();
                }
            } catch (IOException e) {
                throw new ApplicationException(ErrorCode.IO_EXCEPTION,
                    filename, reader.getLineNumber() + 1, e.getMessage())
                    .suppress(e);
            }
        } catch (FileNotFoundException e) {
            throw new ApplicationException(ErrorCode.FILE_NOT_FOUND, filename).suppress(e);
        } catch (IOException e) {
            // Just "swallow" the exception on close the file
        }

        return new OrgStructure(employees, subordination, ceoId);
    }

}
