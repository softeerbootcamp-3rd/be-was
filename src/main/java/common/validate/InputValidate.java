package common.validate;

import common.exception.EmptyFormException;
import common.util.Util;

public class InputValidate {

    public static void validateUserInfo(String query) {
        String[] paramters = Util.splitParamters(query);
        for (String parameter : paramters) {
            String[] tokens = Util.splitParameter(parameter);
            if (tokens.length == 1) {
                throw new EmptyFormException();
            }
        }
    }
}
