// js/login.js

document.addEventListener("DOMContentLoaded", function () {
    const loginButton = document.getElementById("loginButton");

    // 쿠키에서 "sid" 값을 가져옵니다.
    const sid = getCookie("SID");

    // "sid"가 유효하면 로그인 상태로 간주하고 버튼을 숨깁니다.
    if (sid) {
        loginButton.style.display = "none";
    } else {
        loginButton.style.display = "block";
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

