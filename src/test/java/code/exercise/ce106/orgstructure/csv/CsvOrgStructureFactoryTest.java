package code.exercise.ce106.orgstructure.csv;

import code.exercise.ce106.common.ApplicationException;
import code.exercise.ce106.common.ErrorCode;
import code.exercise.ce106.orgstructure.model.OrgStructure;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CsvOrgStructureFactoryTest {

    private final EmployeeCsvRowConverter converter = new EmployeeCsvRowConverter();

    @Test
    void shouldCreateOrgStructureWhenCSVFileHasHeader() {
        var csvOrgStructureFactory = new CsvOrgStructureFactory("./src/test/resources/short.csv", true, converter);

        assertOrgStructure(csvOrgStructureFactory.createOrgStructure());
    }

    @Test
    void shouldCreateOrgStructureWhenCSVFileWithoutHeader() {
        var csvOrgStructureFactory =
            new CsvOrgStructureFactory("./src/test/resources/short_without_header.csv", false, converter);

        assertOrgStructure(csvOrgStructureFactory.createOrgStructure());
    }

    @Test
    void shouldThrowExceptionWhenCatchIOException() {
        // Because the converter is called inside a read-convert loop,
        // we can use a converter mock to simulate an I/O error
        var converterMock = new CsvRowConverter() {
            @Override
            public Object convert(String row) {
                throwException(new IOException("Fake IO exception"));
                return null;
            }
            @SuppressWarnings("unchecked")
            private static <T extends Throwable> void throwException(Throwable e) throws T {
                throw (T) e;
            }
        };
        var csvOrgStructureFactory = new CsvOrgStructureFactory("./src/test/resources/short.csv", true, converterMock);

        var e = assertThrowsExactly(ApplicationException.class, csvOrgStructureFactory::createOrgStructure);

        assertEquals(ErrorCode.IO_EXCEPTION, e.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenNoExistsCSVFile() {
        var csvOrgStructureFactory = new CsvOrgStructureFactory(UUID.randomUUID().toString(), true, converter);

        var e = assertThrowsExactly(ApplicationException.class, csvOrgStructureFactory::createOrgStructure);

        assertEquals(ErrorCode.FILE_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void shouldThrowExceptionWhenCSVFileIsBig() {
        var csvOrgStructureFactory = new CsvOrgStructureFactory("./src/test/resources/1001.csv", true, converter);

        var e = assertThrowsExactly(ApplicationException.class, csvOrgStructureFactory::createOrgStructure);

        assertEquals(ErrorCode.ROWS_LIMIT, e.getErrorCode());
    }

    private void assertOrgStructure(OrgStructure orgStructure) {
        assertNotNull(orgStructure);
        assertEquals("123", orgStructure.ceoId());

        var employees = orgStructure.employees();

        assertNotNull(employees);
        assertEquals(3, employees.size());
        assertNotNull(employees.get("123"));
        assertNotNull(employees.get("124"));
        assertNotNull(employees.get("125"));

        var subordination = orgStructure.subordination();

        assertNotNull(subordination);
        assertEquals(1, subordination.size());

        var subordinates = subordination.get("123");

        assertNotNull(subordinates);
        assertEquals(2, subordinates.size());
        assertTrue(subordinates.containsAll(List.of("124", "125")));
    }

}
