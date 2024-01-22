# OOP (Object Oriented Programming; 객체 지향 프로그래밍)
## OOP (Object Oriented Programming; 객체 지향 프로그래밍)
> OOP의 목적
>
> * 복잡한 문제를 해결하기 위해 현실 세계의 개체(Object)를 소프트웨어 객체(Object)로 모델링함으로써 문제를 더욱 직관적이고 구조화된 방식으로 다룰 수 있다.
> * 주요 목적은 소프트웨어 개발을 보다 효율적이고 유지보수 가능한 방식으로 수행하는 것
### Abstraction(추상화)
* 복잡한 시스템을 단순화하여 핵심적인 개념과 기능에 집중하는 것
* 인터페이스나 추상 클래스를 사용하여 구현
  * 인터페이스: 다른 클래스에서 특정 메서드의 구현을 강제하는 역할
    * 다중 상속이 불가능한 자바에서, 인터페이스를 통해 일종의 "다중 상속" 구현 가능
  * 추상 클래스: 일종의 "템플릿" 역할을 하며, 클래스 간의 공통된 기능을 제공하기 위해 사용
    * 일부 메서드를 구현하고, 일부 메서드를 하위 클래스에서 구현하도록 하는 클래스
    * 추상 클래스는 인스턴스화할 수 없기 때문에, 하위 클래스를 통해 객체를 생성해야 한다
    * 자바는 다중 상속이 불가능하다

### Encapsulation(캡슐화)
* 관련된 데이터와 기능을 하나의 단위로 묶고, 외부로부터의 접근을 제한하는 것
* 객체의 직접적인 접근을 막고 외부에서 내부의 정보에 직접접근하거나 변경할 수 없고 객체가 제공하는 필드와 메소드를 통해서만 접근이 가능하도록 구현
* 인터페이스를 통한 구현 은닉, 접근 제한자를 이용한 데이터 은닉
```java
public class BankAccount {
    // private 접근 제한자를 통해 외부에서의 직접적인 접근 제한
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public double getBalance() {
        return balance;
    }
}

```

### Inheritance(상속)
* 기존에 정의된 클래스의 속성과 메서드를 다른 클래스가 상속받아 사용할 수 있도록 하는 것
```java
class Animal {
    void eat() {
        System.out.println("Animal is eating");
    }
}
// Dog 클래스는 Animal 클래스를 상속받아 eat 메서드를 재사용하고 bark 메서드를 추가로 정의
class Dog extends Animal {
    void bark() {
        System.out.println("Dog is barking");
    }
}

```

### Polymorphism(다형성)
* 하나의 인터페이스나 클래스를 사용하여 여러 가지 타입을 다룰 수 있는 것
* 오버로딩과 오버라이딩 / 인터페이스, 추상 클래스를 사용해 구현
  * 오버로딩(Overloading): 같은 이름의 메서드를 다른 매개변수와 함께 여러 개 정의하는 것, 다양한 매개변수에 대해 다른 동작을 수행
  * 오버라이딩(Overriding): 상위 클래스의 메서드를 하위 클래스에서 재정의하여 사용하는 것

## SOLID 설계 원칙
### 단일 책임 원칙; SRP (Single Responsibility Principle)
* 클래스(객체)는 단 하나의 책임(기능)만 가져야 한다
  * 책임? == 변경의 이유
* 자동차 클래스는 운전 기능, 연료 관리, 속도 제어 등 다양한 기능을 담당할 수 있지만 SRP에 따라 각각의 기능은 별도의 클래스로 분리하여 단일 책임을 갖도록 해야 한다

```java
// 나쁜 예
class User {
    void saveUser() {
        // 사용자를 데이터베이스에 저장하는 코드
    }

    void sendEmail() {
        // 사용자에게 이메일을 보내는 코드
    }
}

// 좋은 예
class User {
    void saveUser() {
        // 사용자를 데이터베이스에 저장하는 코드
    }
}

class EmailService {
    void sendEmail(User user) {
        // 사용자에게 이메일을 보내는 코드
    }
}

```

### 개방-폐쇄 원칙; OCP (Open Closed Principle)
* 확장에 열려있어야 하며, 수정에는 닫혀있어야 한다 -> 인터페이스, 추상화, 상속
* 도형 클래스 - 원, 사각형, 삼각형 클래스: 새로운 도형이 추가되더라도 도형 클래스를 수정하지 않고, 인터페이스를 구현하는 새로운 클래스를 추가하여 기능을 확장할 수 있다
```java
// 나쁜 예
class Shape {
    void draw() {
        // 도형을 그리는 코드
    }
}

// 이후에 새로운 도형 추가 시 수정이 필요함

// 좋은 예
interface Shape {
    void draw();
}

class Circle implements Shape {
    void draw() {
        // 원을 그리는 코드
    }
}

class Square implements Shape {
    void draw() {
        // 사각형을 그리는 코드
    }
}

```

### 리스코프 치환 원칙; LSP (Listov Substitution Principle)
* 상속 관계에서 하위 클래스는 상위 클래스의 기능을 변경하지 않고 확장만 해야 한다
  * 서브 타입은 언제나 기반(부모) 타입으로 교체할 수 있어야 한다
* 동물 클래스 - 강아지 클래스: 동물 클래스가 소리를 내는 기능을 갖고 있을 때, 강아지 클래스는 해당 기능을 상속받아 사용할 수 있어야 한다
  * 부모 타입으로 메서드를 실행해도 의도대로 실행되어야 한다 -> 다형성

```java
// 나쁜 예
class Bird {
    void fly() {
        // 날다
    }
}

class Ostrich extends Bird {
    // 날지 못하는 타조
}

// 좋은 예
interface Bird {
    void fly();
}

class Sparrow implements Bird {
    void fly() {
        // 참새가 날다
    }
}

class Ostrich implements Bird {
    void fly() {
        // 타조도 날지 못하지만, 인터페이스를 따르고 있음
    }
}

```

### 인터페이스 분리 원칙; ISP (Interface Segregation Principle)
* 클라이언트가 자신이 사용하지 않는 인터페이스에 의존하지 않아야 한다
  * 인터페이스의 단일 책임 강조
* 전자기기 인터페이스는 전원 켜기, 전원 끄기, 볼륨 조절 등 다양한 역할에 따라 분리되어 클라이언트가 필요한 기능에만 의존할 수 있도록 해야 한다
```java
// 나쁜 예
interface Worker {
    void work();

    void eat();
}

class Robot implements Worker {
    void work() {
        // 로봇이 일하는 코드
    }

    void eat() {
        // 로봇이 먹는 코드, 로봇이 먹는 것은 의미가 없음
    }
}

// 좋은 예
interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

class Robot implements Workable {
    void work() {
        // 로봇이 일하는 코드
    }
}

```

### 의존 역전 원칙; DIP (Dependency Inversion Principle)
* 상위 모듈은 하위 모듈에 의존해서는 안 되며, 둘 모두 추상화에 의존해야 한다
  * 어떤 Class를 참조해서 사용해야하는 상황이 생긴다면, 그 Class를 직접 참조하는 것이 아니라 그 대상의 상위 요소(추상 클래스 or 인터페이스)로 참조해라
  * 의존 관계를 맺을 때 변화하기 어려운 것 거의 변화가 없는 것에 의존해라 -> 각 클래스간의 결합도를 낮추기 위함
* 커피 메이커 클래스는 커피 원두와 물을 사용하여 커피를 만듭니다. DIP에 따라 커피 원두와 물을 추상화한 인터페이스를 만들고, 커피 메이커 클래스는 이 인터페이스에 의존하여 커피를 만들도록 한다
  * 추상화에 의존하므로 커피 원두와 물을 사용하는 방식이 변경되어도 커피 메이커 클래스를 수정할 필요가 없다

```java
// 나쁜 예
class LightBulb {
    void turnOn() {
        // 전구를 켜는 코드
    }
}

class Switch {
    private LightBulb bulb;

    Switch(LightBulb bulb) {
        this.bulb = bulb;
    }

    void operate() {
        bulb.turnOn();
    }
}

// 좋은 예
interface Switchable {
    void turnOn();
}

class LightBulb implements Switchable {
    void turnOn() {
        // 전구를 켜는 코드
    }
}

class Switch {
    private Switchable device;

    Switch(Switchable device) {
        this.device = device;
    }

    void operate() {
        device.turnOn();
    }
}

```

***
## Reference
- [객체 지향 설계의 5가지 원칙 - S.O.L.I.D](https://inpa.tistory.com/entry/OOP-%F0%9F%92%A0-%EA%B0%9D%EC%B2%B4-%EC%A7%80%ED%96%A5-%EC%84%A4%EA%B3%84%EC%9D%98-5%EA%B0%80%EC%A7%80-%EC%9B%90%EC%B9%99-SOLID)
