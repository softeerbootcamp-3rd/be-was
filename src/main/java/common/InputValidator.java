package common;

import common.exception.EmptyFormException;

import static common.util.Util.split;
import static webserver.RequestParser.PARAMETERS_DELIMITER;
import static webserver.RequestParser.PARAMETER_DELIMITER;

public class InputValidator {

    public static void validateUserInfo(String query) {
        String[] paramters = split(query, PARAMETERS_DELIMITER);
        for (String parameter : paramters) {
            String[] tokens = split(parameter, PARAMETER_DELIMITER);
            if (tokens.length == 1) {
                throw new EmptyFormException();
            }
        }
    }
}
