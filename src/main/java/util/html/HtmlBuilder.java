package util.html;

import constant.HtmlTemplate;
import model.User;
import util.web.SharedData;

public class HtmlBuilder {

    public static byte[] process(byte[] fileContent) {
        String result = new String(fileContent);
        User user = SharedData.requestUser.get();

        if (user != null) {
            for (HtmlTemplate template : HtmlTemplate.values()) {
                if (template.getOriginalValue() == null
                || !result.contains(template.getOriginalValue())) continue;
                result = result.replace(template.getOriginalValue(),
                        template.getLoggedInFunction().apply(template.getTemplate()));
            }
            return result.getBytes();
        }

        for (HtmlTemplate template : HtmlTemplate.values()) {
            if (template.getOriginalValue() == null
            || !result.contains(template.getOriginalValue())) continue;
            result = result.replace(template.getOriginalValue(),
                    template.getLoggedOutFunction().apply(template.getTemplate()));
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
