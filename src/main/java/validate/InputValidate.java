package validate;

import util.Util;

public class InputValidate {

    public static void validateUserInfo(String query) {
        String[] paramters = Util.splitParamters(query);
        for (String parameter : paramters) {
            String[] tokens = Util.splitParameter(parameter);
            if (tokens.length == 1 || tokens[1].isBlank()) {
                throw new IllegalArgumentException("회원가입 시, 모든 항목을 입력해야 합니다.");
            }
        }
    }
}
