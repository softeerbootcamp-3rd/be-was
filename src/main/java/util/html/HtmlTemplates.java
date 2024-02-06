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

        // post
        builder.put("<!--post-list-->",
                new HtmlTemplate("post/list.html", PostHtml::postList, PostHtml::postList));
        builder.put("<!--post-pagination-->",
                new HtmlTemplate("post/pagination.html", PostHtml::pagination, PostHtml::pagination));
        builder.put("<!--post-btn-->",
                new HtmlTemplate("post/post_btn.html", HtmlBuilder::getRaw, HtmlBuilder::getRaw));
        builder.put("<!--post-->",
                new HtmlTemplate("post/post.html", PostHtml::postContent, PostHtml::postContent));
        builder.put("<!--attachment-->",
                new HtmlTemplate("post/attachment.html", PostHtml::attachment, PostHtml::attachment));
        builder.put("<!--preview-->",
                new HtmlTemplate("post/preview.html", PostHtml::preview, HtmlBuilder::empty));
        builder.put("<!--post-btn-group-->",
                new HtmlTemplate("post/btn_group.html", PostHtml::postBtnGroup, HtmlBuilder::empty));
        builder.put("<!--comments-->",
                new HtmlTemplate("post/comments.html", PostHtml::comments, PostHtml::comments));
        builder.put("<!--comment-btn-group-->",
                new HtmlTemplate("post/comment_btn_group.html", null, null));
        builder.put("<!--comment-form-->",
                new HtmlTemplate("post/comment_form.html", PostHtml::commentForm, HtmlBuilder::empty));

        HTML_TEMPLATES = builder.build();
    }

    public static Map<String, HtmlTemplate> values() {
        return HTML_TEMPLATES;
    }

    public static HtmlTemplate get(String key) {
        return HTML_TEMPLATES.get(key);
    }
}
