post = JSON.parse(post);
$(".qna-title").text(post.title);
$(".article-author-name").text(post.author);
$(".article-author-name").attr("href", "/user/profile?postId=${post.id}");
$(".article-header-time").text(post.createdAt);
const articleDocDiv = $('.article-doc');

var tbody = document.querySelector(".article-doc");
tbody.innerHTML = post.contents.map((content, index) =>
        `<p>${content}</p>`
).join('');


