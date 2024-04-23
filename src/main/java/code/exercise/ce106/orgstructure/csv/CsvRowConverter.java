package code.exercise.ce106.orgstructure.csv;

public interface CsvRowConverter<T> {

    T convert(String row);

}
