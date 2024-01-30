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

        // board
        builder.put("<!--board-list-->",
                new HtmlTemplate("board/list.html", BoardHtml::boardList, BoardHtml::boardList));
        builder.put("<!--board-pagination-->",
                new HtmlTemplate("board/pagination.html", BoardHtml::pagination, BoardHtml::pagination));
        builder.put("<!--board-btn-->",
                new HtmlTemplate("board/post_btn.html", HtmlBuilder::getRaw, HtmlBuilder::getRaw));
        builder.put("<!--board-->",
                new HtmlTemplate("board/board.html", BoardHtml::boardContent, BoardHtml::boardContent));
        builder.put("<!--attachment-->",
                new HtmlTemplate("board/attachment.html", BoardHtml::attachment, BoardHtml::attachment));
        builder.put("<!--preview-->",
                new HtmlTemplate("board/preview.html", BoardHtml::preview, HtmlBuilder::empty));
        builder.put("<!--board-btn-group-->",
                new HtmlTemplate("board/btn_group.html", BoardHtml::boardBtnGroup, HtmlBuilder::empty));
        builder.put("<!--comments-->",
                new HtmlTemplate("board/comments.html", BoardHtml::comments, BoardHtml::comments));
        builder.put("<!--comment-btn-group-->",
                new HtmlTemplate("board/comment_btn_group.html", null, null));
        builder.put("<!--comment-form-->",
                new HtmlTemplate("board/comment_form.html", BoardHtml::commentForm, HtmlBuilder::empty));

        HTML_TEMPLATES = builder.build();
    }

    public static Map<String, HtmlTemplate> values() {
        return HTML_TEMPLATES;
    }

    public static HtmlTemplate get(String key) {
        return HTML_TEMPLATES.get(key);
    }
}
