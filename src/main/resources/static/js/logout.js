document.getElementById("logoutButton").addEventListener("click", function() {
    // AJAX 요청 생성
    var xhr = new XMLHttpRequest();

    // 로그아웃 요청을 서버에 보냄
    xhr.open("GET", "/user/logout", true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // 로그아웃이 성공하면 페이지를 새로고침하거나 로그인 페이지로 이동할 수 있습니다.
            window.location.reload();
        }
    };
    xhr.send();
});