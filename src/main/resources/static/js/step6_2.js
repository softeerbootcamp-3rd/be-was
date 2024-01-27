document.addEventListener("DOMContentLoaded", function() {
  // 쿠키에서 'sid' 값을 찾는 함수
  function getCookie(name) {
    let cookieValue = null;
    if (document.cookie && document.cookie !== '') {
      const cookies = document.cookie.split(';');
      for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].trim();
        // 쿠키 이름이 매개변수와 같은지 확인
        if (cookie.substring(0, name.length + 1) === (name + '=')) {
          cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
          break;
        }
      }
    }
    return cookieValue;
  }

  // 'sid' 쿠키가 있는지 확인
  if (!getCookie('sid')) {
    // 'sid' 쿠키가 없으면 로그인 페이지로 리다이렉트
    window.location.href = 'login.html';
  } else {
    // 'sid' 쿠키가 있으면 /user/list API 호출
    fetch('/user/list')
      .then(response => response.json())
      .then(data => {
        const tbody = document.querySelector('.table tbody');
        tbody.innerHTML = ''; // 기존 행 삭제
        data.forEach((user, index) => {
          // 새로운 행 추가
          const row = `<tr>
                        <th scope="row">${index + 1}</th>
                        <td>${user.userId}</td>
                        <td>${user.userName}</td>
                        <td>${user.email}</td>
                        <td><a href="#" class="btn btn-success" role="button">수정</a></td>
                      </tr>`;
          tbody.innerHTML += row;
        });
      })
      .catch(error => console.error('Error:', error));
  }
});
