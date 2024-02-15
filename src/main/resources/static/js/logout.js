document.addEventListener("DOMContentLoaded", function () {

    var logoutButton  = document.getElementById("logoutButton");

    if (logoutButton) {
        logoutButton.addEventListener("click", function () {
            // 로그아웃 요청을 서버에 보냄
            fetch("/user/logout", {
                method: "POST",
                credentials: "same-origin" // 브라우저의 쿠키를 request에 포함하기 위해
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`Network response was not ok: ${response.status}`);
                    }
                    return response.text(); // 서버가 보낸 response 파싱 가능
                })
                .then(() => {
                    // 로그아웃 성공하면 페이지 reload
                    window.location.reload();
                })
                .catch(error => {
                    console.error('There was a problem with the fetch operation:', error);
                });
        });
    }


});
