// 로그아웃 버튼에 클릭 이벤트 리스너를 추가합니다.
document.getElementById('logoutButton').addEventListener('click', function(event) {
    // 기본 앵커 태그 동작을 방지합니다.
    event.preventDefault();

    // 'sid' 쿠키를 삭제하는 함수를 호출합니다.
    deleteCookie('sid');

    window.location.href = 'user/login.html';

});

// 쿠키를 삭제하는 함수입니다.
function deleteCookie(name) {
    // 쿠키를 삭제하기 위해 만료 날짜를 과거로 설정합니다.
    document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/';
}
