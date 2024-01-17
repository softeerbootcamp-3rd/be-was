# java-was-2023

Java Web Application Server 2023

## 프로젝트 정보 

이 프로젝트는 우아한 테크코스 박재성님의 허가를 받아 https://github.com/woowacourse/jwp-was 
를 참고하여 작성되었습니다.

## 🐧공부한 것
### 💡소켓 통신
소켓 번호를 통해 byte stream으로 통신

[서버 클래스(`WebServer.java`)에 필요한 것]

> 서버 소켓

클라이언트의 요청을 받기 위한 준비

> 소켓

실제 데이터 주고 받기

</br>

### 💡자바 스레드 모델

자바 프로그램 실행 → JVM 프로세스 실행 → JVM 프로세스 안에서 여러 개의 스레드 실행

- 각 스레드마다 Java Stack, PC register, Native Method stack

**Why thread?**

- 프로세스 자원 많이 필요(최소 32MB)
- 스레드는 1MB이내

**Multi Threading Model**

- `User Level Thread`
    - 사용자 라이브러리로 사용자가 만든 스레드
    - 스레드가 생성된 프로세스 자체에 의해 제어됨, 커널은 사용자 스레드에 대해 모름
- `Kernel Level Thread
    - 커널에 의해 생성되고 운영체제가 관리
    - OS 제어
- 스레드를 실행하기 위해서는 CPU 스케줄러(커널)이 CPU 스케줄링 해야함.
    - 근데 앞에서 커널은 사용자 스레드에 대해 알지 못한다고 나와 있음
    - →사용자 스레드는 커널 스레드에 매핑되어 실행됨
- 커널 스레드와 사용자 스레드의 매핑 방법
    - Many-to-One Model
  
      ![Untitled.png](..%2F..%2FDownloads%2FUntitled.png)
        - 한 스레드가 blocking 되면 전체 프로세스가 block
        - 한번에 하나의 스레드만 커널에 접근할 수 있어 멀티 코어 시스템에서는 병렬로 실행될 수 없음(멀티 코어의 이점을 살릴 수 없음)
        - 동기화 및 리소스 공유가 쉬워 실행시간 단축됨
        - 초기 버전 스레드 모델인 Green Thread

    - One-to-One Model

      ![Untitled (1).png](..%2F..%2FDownloads%2FUntitled%20%281%29.png)

        - 하나의 스레드 blocking 되어도 다른 스레드가 실행될 수 있어 병렬 실행이 용이
        - 단점은 사용자 스레드를 위해 커널 스레드를 만들어야 한다는 것
            - 많은 수의 커널 스레드는 시스템 성능에 부담
        - 대부분의 운영체제
    - Many-to-Many Model

      ![Untitled (2).png](..%2F..%2FDownloads%2FUntitled%20%282%29.png)
        - 앞의 두 모델 단점 절충
        - 가장 높은 정확도의 동시성 처리 제공
        - aka. two-level-model
            - 일대일 모델로의 동작도 허용하기 때문
        - 구현이 어려움


자바의 스레드 모델: 다대다 모델(Native Thread)
- by OS
- since java 1.2

### 💡 유틸리티 클래스
인스턴스 메서드와 변수가 없고, 오로지 정적 메서드와 변수만 있는 클래스.
비슷한 기능의 메서드와 상수를 캡슐화 하는 의의

## 🐧 STEP 1 구현 방법
### 💡 정적인 html 파일 응답
FileReader 유틸클래스에서 Files.readAllBytes를 통해 html 파일 읽어옴.
### 💡 HTTP Request 출력
RequestParser라는 유틸클래스를 만들어 Request Path, Host, Method, Accept, User-Agent, Cookie 정보 파싱
### 💡 Concurrent Package 사용
WebServer에서 ExecutorService를 사용하여 스레드 제어

