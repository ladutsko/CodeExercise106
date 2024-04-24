package code.exercise.ce106.orgstructure.csv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeCsvRowConverterTest {

    private final EmployeeCsvRowConverter converter = new EmployeeCsvRowConverter();

    @Test
    void shouldConvertSuccessfulWhenManagerIdAbsent() {
        var employee = converter.apply(" 123 , Joe , Doe , 60000 , ");

        assertEquals("123", employee.id());
        assertEquals("Joe", employee.firstName());
        assertEquals("Doe", employee.lastName());
        assertEquals(new BigDecimal("60000"), employee.salary());
        assertNull(employee.managerId());
    }

    @Test
    void shouldConvertSuccessfulWhenManagerIdPresent() {
        var employee = converter.apply(" 124 , Martin , Chekov , 45000 , 123");

        assertEquals("124", employee.id());
        assertEquals("Martin", employee.firstName());
        assertEquals("Chekov", employee.lastName());
        assertEquals(new BigDecimal("45000"), employee.salary());
        assertEquals("123", employee.managerId());
    }

    @Test
    void shouldThrowExceptionWhenLessCols() {
        var e = assertThrowsExactly(IllegalArgumentException.class, () ->
            converter.apply("123,Joe,Doe,60000"));

        assertTrue(e.getMessage().contains("Unexpected cols count: 4"));
    }

    @Test
    void shouldThrowExceptionWhenMoreCols() {
        var e = assertThrowsExactly(IllegalArgumentException.class, () ->
            converter.apply("123,Joe,Doe,60000,121,extra"));

        assertTrue(e.getMessage().contains("Unexpected cols count: 6"));
    }

    @Test
    void shouldThrowExceptionWhenNegativeSalary() {
        var e = assertThrowsExactly(IllegalArgumentException.class, () ->
                converter.apply("123,Joe,Doe,-60000,"));

        assertTrue(e.getMessage().contains("salary must be positive"));
    }

    @Test
    void shouldThrowExceptionWhenZeroSalary() {
        var e = assertThrowsExactly(IllegalArgumentException.class, () ->
                converter.apply("123,Joe,Doe,0,"));

        assertTrue(e.getMessage().contains("salary must be positive"));
    }

    @ParameterizedTest
    @MethodSource("provideForShouldThrowExceptionWhenBlankColValue")
    void shouldThrowExceptionWhenBlankColValue(String line, String colName) {
        var e = assertThrowsExactly(IllegalArgumentException.class, () -> converter.apply(line));

        assertTrue(e.getMessage().contains(colName + " must not be blank"));
    }

    private static Stream<Arguments> provideForShouldThrowExceptionWhenBlankColValue() {
        return Stream.of(
            Arguments.of(" ,Joe,Doe,60000, ", "Id"),
            Arguments.of("123, ,Doe,60000, ", "firstName"),
            Arguments.of("123,Joe, ,60000, ", "lastName"),
            Arguments.of("123,Joe,Doe, , ", "salary")
        );
    }

}
