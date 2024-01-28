package util.html;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class HtmlTemplates {

    public static final ImmutableMap<String, HtmlTemplate> HTML_TEMPLATES;

    static {
        ImmutableMap.Builder<String, HtmlTemplate> builder = ImmutableMap.builder();

        // common
        builder.put("<!--login-btn-->",
                new HtmlTemplate("common/login_btn.html", HtmlBuilder::empty, HtmlBuilder::getRaw));
        builder.put("<!--login-btn-active-->",
                new HtmlTemplate("common/login_btn_active.html", HtmlBuilder::empty, HtmlBuilder::getRaw));
        builder.put("<!--logout-btn-->",
                new HtmlTemplate("common/logout_btn.html", HtmlBuilder::getRaw, HtmlBuilder::empty));
        builder.put("<!--signup-btn-->",
                new HtmlTemplate("common/signup_btn.html", HtmlBuilder::empty, HtmlBuilder::getRaw));
        builder.put("<!--signup-btn-active-->",
                new HtmlTemplate("common/signup_btn_active.html", HtmlBuilder::empty, HtmlBuilder::getRaw));

        // user
        builder.put("<!--user-name-->",
                new HtmlTemplate("user/user_name.html", UserHtml::replaceUser, HtmlBuilder::empty));
        builder.put("<!--user-list-->",
                new HtmlTemplate("user/user_list.html", UserHtml::replaceUserList, HtmlBuilder::empty));

        // qna
        builder.put("<!--qna-list-->",
                new HtmlTemplate("qna/list.html", QnaHtml::qnaList, QnaHtml::qnaList));
        builder.put("<!--qna-pagination-->",
                new HtmlTemplate("qna/pagination.html", QnaHtml::pagination, QnaHtml::pagination));
        builder.put("<!--post-qna-btn-->",
                new HtmlTemplate("qna/post_btn.html", HtmlBuilder::getRaw, HtmlBuilder::empty));
        builder.put("<!--qna-->",
                new HtmlTemplate("qna/qna.html", QnaHtml::qnaContent, QnaHtml::qnaContent));
        builder.put("<!--qna-btn-group-->",
                new HtmlTemplate("qna/btn_group.html", QnaHtml::qnaBtnGroup, HtmlBuilder::empty));
        builder.put("<!--comments-->",
                new HtmlTemplate("qna/comments.html", QnaHtml::comments, QnaHtml::comments));
        builder.put("<!--comment-btn-group-->",
                new HtmlTemplate("qna/comment_btn_group.html", null, null));
        builder.put("<!--comment-form-->",
                new HtmlTemplate("qna/comment_form.html", QnaHtml::commentForm, HtmlBuilder::empty));

        HTML_TEMPLATES = builder.build();
    }

    public static Map<String, HtmlTemplate> values() {
        return HTML_TEMPLATES;
    }

    public static HtmlTemplate get(String key) {
        return HTML_TEMPLATES.get(key);
    }
}
