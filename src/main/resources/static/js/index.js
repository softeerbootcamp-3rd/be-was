var posts = JSON.parse(posts);

var list = document.querySelector(".list");
list.innerHTML = posts.map((post, index) =>
`<li>
    <div class="wrap">
        <div class="main">
            <strong class="subject">
                <a href="/qna/detail?id=${post.id}">${post.title}</a>
            </strong>
            <div class="auth-info">
                <i class="icon-add-comment"></i>
                <span class="time">${post.createdAt}</span>
                <a href="./user/profile?postId=${post.id}" class="author">${post.author}</a>
            </div>
            <div class="reply" title="댓글">
                <i class="icon-reply"></i>
                <span class="point">${post.id}</span>
            </div>
        </div>
    </div>
</li>`
).join('');