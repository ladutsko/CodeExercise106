package code.exercise.ce106.context.arguments;

public final class ArgumentsConstants {

    public static final boolean DEFAULT_HAS_HEADER_VAL = true;

    public static final String ARG_PREFIX = "--";

    public static final String WITHOUT_HEADER_ARG = ARG_PREFIX + "without-header";

    private ArgumentsConstants() {
        // There are no use cases for this class where you need to build an object. You can only use static items.
        // I am preventing you from even trying to build an object of this class.
    }

}
