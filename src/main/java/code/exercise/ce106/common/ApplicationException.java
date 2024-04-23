package code.exercise.ce106.common;

import java.io.Serial;

public class ApplicationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3469312053460203274L;

    private final ErrorCode errorCode;

    public ApplicationException(ErrorCode errorCode, Object... args) {
        super(errorCode.formatted(args));
        this.errorCode = errorCode;
    }

    public ApplicationException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(errorCode.formatted(args), cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public ApplicationException suppress(Throwable exception) {
        addSuppressed(exception);
        return this;
    }

}
