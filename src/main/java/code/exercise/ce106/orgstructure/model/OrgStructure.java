package code.exercise.ce106.orgstructure.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record OrgStructure(
    Map<String, Employee> employees,
    Map<String, List<String>> subordination,
    String ceoId
) {

    public OrgStructure(Map<String, Employee> employees, Map<String, List<String>> subordination, String ceoId) {
        this.employees = Map.copyOf(employees);
        this.subordination = Collections.unmodifiableMap(subordination
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entity -> List.copyOf(entity.getValue()))));
        this.ceoId = ceoId;
    }

}
