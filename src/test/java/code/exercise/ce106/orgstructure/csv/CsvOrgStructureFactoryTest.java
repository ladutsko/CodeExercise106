package code.exercise.ce106.orgstructure.csv;

import code.exercise.ce106.orgstructure.model.OrgStructure;
import code.exercise.ce106.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CsvOrgStructureFactoryTest {

    private final EmployeeCsvRowConverter converter = new EmployeeCsvRowConverter();

    @Test
    void shouldCreateOrgStructureWhenCSVFileCorrect() {
        var resource = FileUtil.createResource("./src/test/resources/short.csv");
        var orgStructureFactory = new CsvOrgStructureFactory(resource, converter);

        OrgStructure orgStructure = orgStructureFactory.createOrgStructure();

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

    @Test
    void shouldThrowExceptionWhenNoExistsCSVFile() {
        var filename = UUID.randomUUID().toString();
        var resource = FileUtil.createResource(filename);
        var orgStructureFactory = new CsvOrgStructureFactory(resource, converter);

        var e = assertThrowsExactly(UncheckedIOException.class,
            orgStructureFactory::createOrgStructure);

        assertTrue(e.getMessage().contains("java.io.FileNotFoundException: " + filename));
    }

    @Test
    void shouldThrowExceptionWhenCSVFileIsBig() {
        var resource = FileUtil.createResource("./src/test/resources/1001.csv");
        var orgStructureFactory = new CsvOrgStructureFactory(resource, converter);

        var e = assertThrowsExactly(UncheckedIOException.class,
                orgStructureFactory::createOrgStructure);

        assertTrue(e.getMessage().contains("java.io.IOException: Number of rows can be up to 1000"));
    }

    @Test
    void souldThrowExceptionWhenCSVFileHasDoubleEmployeeId() {
        var resource = FileUtil.createResource("./src/test/resources/double_employee.csv");
        var orgStructureFactory = new CsvOrgStructureFactory(resource, converter);

        var e = assertThrowsExactly(IllegalStateException.class,
                orgStructureFactory::createOrgStructure);

        assertTrue(e.getMessage().contains("Employee with Id '123' already exists"));
    }

    @Test
    void souldThrowExceptionWhenCSVFileHasDoubleCEO() {
        var resource = FileUtil.createResource("./src/test/resources/double_ceo.csv");
        var orgStructureFactory = new CsvOrgStructureFactory(resource, converter);

        var e = assertThrowsExactly(IllegalStateException.class,
                orgStructureFactory::createOrgStructure);

        assertTrue(e.getMessage().contains("CEO with Id '123' already defined"));
    }

    @Test
    void souldThrowExceptionWhenCSVFileHasNoCEO() {
        var resource = FileUtil.createResource("./src/test/resources/without_ceo.csv");
        var orgStructureFactory = new CsvOrgStructureFactory(resource, converter);

        var e = assertThrowsExactly(IllegalStateException.class,
                orgStructureFactory::createOrgStructure);

        assertTrue(e.getMessage().contains("CEO is undefined"));
    }

}
