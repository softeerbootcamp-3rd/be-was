// js/login.js

document.addEventListener("DOMContentLoaded", function () {
    let isLoggedIn = false;
    const loginButton = document.getElementById("loginButton");
    const userNameElement = document.getElementById("userName");

    // 쿠키에서 SID 값 가져오기
    const sid = getCookie("SID");

    // SID 값이 있으면 로그인 한 상태
    if (sid) {
        isLoggedIn = true;
    } else {
        isLoggedIn = false;
    }

    // 로그인 O : 로그인 버튼 숨김, 사용자 이름 표시
    // 로그인 X : 로그인 버튼 표시, 사용자 이름 X
    if (isLoggedIn) {
        loginButton.style.display = "none";
        userNameElement.textContent = "사용자 이름";
        userNameElement.style.display = "inline-block";
    } else {
        loginButton.style.display = "block";
        userNameElement.style.display = "none";
    }

    // 쿠키에서 특정 이름의 쿠키 값을 가져오는 함수
    function getCookie(cookieName) {
        const decodedCookie = decodeURIComponent(document.cookie);
        if (decodedCookie == null)
            return null;
        const name = cookieName + "=";
        const cookieArray = decodedCookie.split(';');
        for (let i = 0; i < cookieArray.length; i++) {
            let cookie = cookieArray[i].trim();
            if (cookie.indexOf(name) === 0) {
                return cookie.substring(name.length, cookie.length);
            }
        }
        return null;
    }

});

