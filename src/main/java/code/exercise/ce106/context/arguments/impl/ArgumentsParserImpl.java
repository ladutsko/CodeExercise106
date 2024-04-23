package code.exercise.ce106.context.arguments.impl;

import code.exercise.ce106.common.ApplicationException;
import code.exercise.ce106.common.ErrorCode;
import code.exercise.ce106.context.arguments.Arguments;
import code.exercise.ce106.context.arguments.ArgumentsConstants;
import code.exercise.ce106.context.arguments.ArgumentsParser;

public class ArgumentsParserImpl implements ArgumentsParser {

    @Override
    public Arguments parse(String[] args) {
        String filename = null;
        boolean hasHeader = ArgumentsConstants.DEFAULT_HAS_HEADER_VAL;

        int i = 0;
        while (args.length > i) {
            String arg = args[i++];

            if (!arg.startsWith(ArgumentsConstants.ARG_PREFIX)) {
                filename = arg;
                break;
            }

            if (ArgumentsConstants.WITHOUT_HEADER_ARG.equals(arg)) {
                hasHeader = false;
            } else {
                throw new ApplicationException(ErrorCode.UNEXPECTED_ARG, arg);
            }
        }

        if (args.length > i) {
            throw new ApplicationException(ErrorCode.UNEXPECTED_ARG, args[i]);
        }

        return new Arguments(filename, hasHeader);
    }

}
