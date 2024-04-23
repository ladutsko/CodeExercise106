package code.exercise.ce106.orgstructure.model;

import java.math.BigDecimal;

public record Employee(
    String id,
    String firstName,
    String lastName,
    BigDecimal salary,
    String managerId
) {
}
