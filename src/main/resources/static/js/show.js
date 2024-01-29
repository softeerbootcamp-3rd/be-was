post = JSON.parse(post);
console.log(post);
$(".qna-title").text(post.title);
$(".article-author-name").text(post.author);
$(".article-header-time").text(post.createdAt);
const articleDocDiv = $('.article-doc');

var tbody = document.querySelector(".article-doc");
console.log(post.contents)
    tbody.innerHTML = post.contents.map((content, index) =>
        `<p>${content}</p>`
    ).join('');


