package code.exercise.ce106.orgstructure.csv;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeCsvRowConverterTest {

    private final EmployeeCsvRowConverter converter = new EmployeeCsvRowConverter();

    @Test
    void shouldConvertSuccessfulWhenManagerIdAbsent() {
        var employee = converter.convert("123,Joe,Doe,60000,");

        assertEquals("123", employee.id());
        assertEquals("Joe", employee.firstName());
        assertEquals("Doe", employee.lastName());
        assertEquals(new BigDecimal("60000"), employee.salary());
        assertNull(employee.managerId());
    }

    @Test
    void shouldConvertSuccessfulWhenManagerIdPresent() {
        var employee = converter.convert("124,Martin,Chekov,45000,123");

        assertEquals("124", employee.id());
        assertEquals("Martin", employee.firstName());
        assertEquals("Chekov", employee.lastName());
        assertEquals(new BigDecimal("45000"), employee.salary());
        assertEquals("123", employee.managerId());
    }
}
