# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.

## 깃의 사실과 오해
Q1. git init을 수행하면 현재디렉토리가 로컬 저장소가된다?
A1. NO, .git이 저장소가된다

Q2. git의 객체에는 무엇이 있을까?
A2. Blob tree commit Tag
commit 안에는 blob과 tree가 들어있다.

Q3. 저장은 어느시점부터될까? 파일 만드는시점? Git add? commit? + 각 명령어의 기능
A3. Git add 
stage는 무엇인가? - 커밋을 만들기위한 준비공간
commit이 뭔데? 일종의 세이브 포인트? 우리의 작업내용의 스냅샷

Q4. 커밋을 하고나면 스테이지는 깨끗하게 비워진다?
A4. 안비워진다.(작업디렉토리와 스테이지와 저장소가 모두 같을때 비어있다고 표현)
마지막 커밋에는 트리가 있고, 그 트리안에는 블롭들이 들어있다.

Q5. 스테이지의 변경사항을 취소하려면 스테이지를 직접 조작하는 명령인 'git rm --cached <파일이름>' 을 사용해서 변경사항을 스테이지에서 제거하면 된다.
A5. YES

Q6. Git status는 [...]들을 비교해서 보여주는 명령어이다
A6. stage HEAD commit

Q7. 파일의 내용을 수정하고 커밋하면 GIT은 공간 효율을 위해 변경사항만 저장한다.
A7. NO (전체 파일이 저장된다.)
깃은 변경사항이 아니라 전체를 저장하기때문에 순식간에 switch가 가능하다. 성능을 위해 전체저장!

Q8. a.txt를 새로 만들고 새로운 디렉토리에 복사한 후 add, commit 한 후 새로운 브랜치를 만들어 변경했다. 이때 blob이 새로 생성되는가?
A8. NO (깃에서 똑같은 파일을 여러개만들어도 더이상의 blob이 생기지않고 하나만 올라간다.)
