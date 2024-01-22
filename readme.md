# 학습내용 정리

## Step 1 - index.html 응답

+ ### 해결해야 했던 문제점
  * 자바 경험이 전무하여 기본 제공되는 소스코드조차 이해하기 어려웠음
  * HTTP, 서버 동작 방식 등 문제와 관련된 각종 개념에 대한 지식의 부족
<br><br>

+ ### 학습한 내용
  + #### 기본 제공되는 소스코드에서 활용되는 각종 자바 함수들의 기능, 역할 공부
    + logger의 역할 : 시스템에 특정 정보 기록하는 역할. 유니티의 log와 유사한 듯
    + accept() : 클라이언트로부터 정보를 받아와 저장하는 inputstream 공간, 요청 브라우저로 출력할 outputstream 공간을 생성
    + Input/OutputStream : 클라이언트가 제공한 데이터 / 출력할 데이터를 담는 임시 공간같은 느낌.
    + DataInput/output Stream : 일단 이 함수를 통해 출력해야 한다는 사실만 학습. 왜 이 함수를 써야하는지는 추가적인 공부 필요..
    + BufferedReader : InputStream에 담긴 데이터를 읽어오는 역할. 성능이 더 좋아서 사용한다고 한다. InputStreamReader를 통해 읽어와야 함.
  + #### 해당 문제에서의 클라이언트-서버 간 대략적인 동작 흐름
    + 클라이언트가 특정 포트를 통해 서버에 요청
    + 서버는 accept() 함수를 통해 클라이언트로부터 정보를 받아와 inputstream에 저장.
    + 받아온 정보(HTTP request) 중 가장 중요한 첫 줄만 logger가 기록함.
    + index.html을 읽어와 outputstream을 통해 요청한 브라우저로 출력.