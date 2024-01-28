package common;

import static http.RequestParser.PARAMETERS_DELIMITER;
import static http.RequestParser.PARAMETER_DELIMITER;

public class InputValidator {

    public static boolean validateForm(String form) {
        String[] paramters = form.split(PARAMETERS_DELIMITER);
        for (String parameter : paramters) {
            String[] tokens = parameter.split(PARAMETER_DELIMITER);
            if (tokens.length == 1) {
                return false;
            }
        }
        return true;
    }
}
