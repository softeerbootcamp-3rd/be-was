package util.html;

import model.User;
import util.web.SharedData;

import java.util.Map;

public class HtmlBuilder {

    public static byte[] process(byte[] fileContent) {
        String result = new String(fileContent);
        User user = SharedData.requestUser.get();

        // 로그인 된 상태
        if (user != null) {
            for (Map.Entry<String, HtmlTemplate> template : HtmlTemplates.values().entrySet()) {
                if (!result.contains(template.getKey())) continue;
                result = result.replace(template.getKey(),
                        template.getValue().getLoggedInFunction().apply(template.getValue().getTemplate()));
            }
            return result.getBytes();
        }

        //로그인 안된 상태
        for (Map.Entry<String, HtmlTemplate> template : HtmlTemplates.values().entrySet()) {
            if (!result.contains(template.getKey())) continue;
            result = result.replace(template.getKey(),
                    template.getValue().getLoggedOutFunction().apply(template.getValue().getTemplate()));
        }
        return result.getBytes();
    }

    public static String empty(String unused) {
        return "";
    }

    public static String getRaw(String template) {
        if (template == null)
            return "";
        return template;
    }

}
