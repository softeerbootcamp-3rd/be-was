package util;

public class HtmlParser {
    private String html;

    public HtmlParser(String html) {
        this.html = html;
    }

    public void appendContentById(String id, String additionalContent) {
        int startIdx = html.indexOf("id=\"" + id + "\"");
        appendHtml(additionalContent, startIdx);
    }

    public void appendContentByClass(String className, String additionalContent) {
        int startIdx = html.indexOf("class=\"" + className + "\"");
        appendHtml(additionalContent, startIdx);
    }

    public void appendContentByTag(String tagName, String additionalContent) {
        int startIdx = html.indexOf("<" + tagName);
        appendHtml(additionalContent, startIdx);
    }
    private void appendHtml(String additionalContent, int startIdx) {
        if (startIdx != -1) {
            int endIdx = html.indexOf('>', startIdx);
            if (endIdx != -1) {
                String divContent = html.substring(endIdx + 1, html.indexOf('<', endIdx));

                divContent += additionalContent;

                html = html.substring(0, endIdx + 1) + divContent + html.substring(html.indexOf('<', endIdx));
            }
        }
    }

    public String getHtml() {
        return html;
    }
}
