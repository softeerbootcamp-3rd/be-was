# 학습내용 정리

## Java Thread

- Java에서의 Thread?
    - JVM 프로세스 안에서는 여러 개의 스레드가 실행됨
    - JVM 메모리는 각 Thread마다 생성되는 영역과 전체가 공유하는 영역으로 나누어짐

<img width="587" alt="Thread1" src="https://github.com/insiderhj/be-was/assets/43667241/1383cdb7-f4f4-43b5-8841-90a7da9a366e">

### Green Thread Model

- Many-to-One
- Lightweight thread, Cooperative thread, User-level thread 등으로 불림
- 운영체제의 지원을 받지 않고, 어플리케이션 수준에서 스레드를 관리하는 방식이다
- 가상 머신이나 런타임 언어에 의해 관리됨
- 비교적 가벼우며, context switching이 어플리케이션 수준에서 제어되기 때문에 더 빠른 스위칭을 지원하지만, 동시성 제어와 관련된 문제가 있긴 하다

<img width="261" alt="Thread2" src="https://github.com/insiderhj/be-was/assets/43667241/518b9c9f-03a6-4976-a55c-797c070bfcb5">

### Native Thread Model

- Many-to-Many
- 운영체제가 제공하는 네이티브 스레드를 활용
- 운영체제의 지원을 받기 때문에 멀티코어, 다중 프로세서 아키텍처에서 효과적임
- **`java.lang.Thread`** 클래스를 통해 스레드를 생성하고 관리
- **`wait()`**, **`notify()`**, **`notifyAll()`** 등을 사용하여 스레드 간 통신 및 동기화를 지원함

<img width="575" alt="Thread3" src="https://github.com/insiderhj/be-was/assets/43667241/396cff4d-d81f-4554-9f7e-8a9a116784d2">

### Virtual Thread

- 경량 스레드로서, 네이티브 스레드에 비해 생성 및 관리가 훨씬 가벼움 (수천 개를 생성해도 운영체제에 부담이 없음)
- 스레드의 스케쥴링을 사용자가 직접 제어할 수 있도록 하는 API 제공
- **`AutoCloseable`** 인터페이스를 구현하여 자동으로 자원을 해제할 수 있음

<img width="661" alt="Thread4" src="https://github.com/insiderhj/be-was/assets/43667241/5962611d-ed34-4b1e-8de5-03ea46545163">

## Java Reflection

SofteerBottcamp-3 `김희진` `정석인`

### Java Reflection이란?

- 컴파일한 클래스 정보를 활용해 동적으로 프로그래밍이 가능하도록 지원하는 API

### 주요 Class

- java.lang.refelct.Constructor
- java.lang.refelct.Field
- java.lang.refelct.Method

### Reflection을 활용해 가능한 작업들

- 클래스 내의 모든 필드, 생성자, 메소드 정보 출력
- 특정 패턴의 이름을 가진 메소드 실행
- 필드, 메소드 등에 특정 어노테이션이 활성화되어 있는지 판단
- 필드에 직접 접근하여 값 설정

### Reflection 활용 예시

1. **Spring Framework**
    - Spring은 IoC (Inversion of Control)와 DI (Dependency Injection)를 구현하기 위해 Reflection을 활용함. Bean 객체를 동적으로 생성하고 프로퍼티를 설정하는 데 Reflection이 사용됨
2. **JUnit**
    - 테스트 클래스와 테스트 메소드를 찾고 실행하는 데 Reflection이 활용됨
3. **Spring Boot**
    - 자동 구성 및 컴포넌트 스캔과 같은 특성을 제공하기 위해 Reflection을 사용함. 클래스 경로에서 컴포넌트를 검색하고 동적으로 빈을 생성하는 데에 활용
4. **JavaBeans Introspector**
    - JavaBean 클래스의 속성 및 이벤트를 동적으로 발견하고 조사하는 데 사용함
5. **Jackson 라이브러리**
    - Jackson은 JSON 데이터를 Java 객체로 매핑하고 반대로 매핑하는 데 Reflection을 사용함. 객체의 필드와 메소드를 동적으로 검사하여 JSON 데이터와 매핑


### Reflection 실습

1. 클래스 정보 출력

    ```java
    @Test
    public void showClass() {
        SoftAssertions s = new SoftAssertions();
        Class<Question> clazz = Question.class;
        logger.debug("Classs Name {}", clazz.getName());
        for(Field field : clazz.getDeclaredFields()) {
            logger.debug("Class field {}", field);
        }
        for(Method method : clazz.getDeclaredMethods()) {
            logger.debug("Class method {}", method);
        }
    }
    ```

2. test로 시작하는 메소드 실행

    ```java
    @Test
    public void runner() throws Exception {
        Class clazz = Junit3Test.class;
        for(Method method : clazz.getDeclaredMethods()) {
            if(method.getName().startsWith("test")) {
                method.invoke(clazz.newInstance());
            }
        }
    }
    ```

3. @Test 어노테이션 메소드 실행

    ```java
    @Test
    public void run() throws Exception {
        Class clazz = Junit4Test.class;
    
        Class<? extends Annotation> annotation = MyTest.class;
        for(Method method : clazz.getDeclaredMethods()) {
            if(method.isAnnotationPresent(annotation)) {
                method.invoke(clazz.newInstance());
            }
        }
    }
    ```

4. private field에 값 할당

    ```java
    @Test
    public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Class<Student> clazz = Student.class;
    
        Student student = new Student();
        Field f = student.getClass().getDeclaredField("name");
        f.setAccessible(true);
        f.set(student, "JAVA");
    
        f = student.getClass().getDeclaredField("age");
        f.setAccessible(true);
        f.set(student, 16);
        logger.debug(student.getName());
        logger.debug(Integer.toString(student.getAge()));
    }
    ```

5. 인자를 가진 생성자의 인스턴스 생성

    ```java
    @Test
    public UserTest() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?>[] userConstructor = clazz.getDeclaredConstructors();
    
        for(Constructor<?> constructor : userConstructor) {
            Class<?>[] types = constructor.getParameterTypes();
    
            if(types.length == 2) {
                if(types[0].equals(String.class) &&
                        types[1].equals(Integer.class)) {
                    User user = (User)constructor.newInstance("JAVA", 15);
                    logger.debug("new instance Initialized");
                    logger.debug(user.getName());
                    logger.debug(user.getAge());
                }
            }
        }
    }
    ```
