package code.exercise.ce106.common;

public enum ErrorCode {

    UNEXPECTED_ARG       ("E01", "Unexpected argument: %s"),

    FILE_NOT_FOUND       ("E11", "File not found: %s"),
    IO_EXCEPTION         ("E12", "IO exception in file '%s' line %d: %s"),
    ROWS_LIMIT           ("E13", "Numbers of rows can be up to %d: %s"),

    UNEXPECTED_EXCEPTION ("E99", "Unexpected exception: %s");

    private final String code;
    private final String messageTemplate;

    ErrorCode(String code, String messageTemplate) {
        this.code = code;
        this.messageTemplate = messageTemplate;
    }

    public String getCode() {
        return code;
    }

    public String formatted(Object... args) {
        return code + ": " + messageTemplate.formatted(args);
    }

}
