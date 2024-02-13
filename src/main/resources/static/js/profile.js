console.log(author)
if (author !== '') {
    author = JSON.parse(author);

    $(".media-heading").text(author.name);
    $(".btn-default").text(author.email);
}
else{
    $(".media-heading").text(userName);
    $(".btn-default").text(userEmail);
}