    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("header").innerHTML = xhr.responseText;

            if(userName){
                var navbar = document.getElementById("navbar");
                navbar.insertAdjacentHTML("afterbegin", "<li><p id='welcome'>안녕하세요 " + userName + "님</p></li>>");

                navbar = document.querySelector("#navbar-collapse2 > ul");
                navbar.insertAdjacentHTML("beforeend", "<li class='active'><a href='/'>Posts</a></li><li><a href='/user/logout' role='button'>로그아웃</a></li>");
            }
            else{
                navbar = document.querySelector("#navbar-collapse2 > ul");
                navbar.insertAdjacentHTML("beforeend", "<li><a href='/user/login' role='button'>로그인</a></li><li><a href='/user/create' role='button'>회원가입</a></li>");
            }
        }
    };

    xhr.open("GET", "/header", true);
    xhr.send();
