# OOP
## OOP (Object Oriented Programming; 객체 지향 프로그래밍)
#### Abstraction(추상화)
#### Encapsulation(캡슐화)
* 객체의 직접적인 접근을 막고 외부에서 내부의 정보에 직접접근하거나 변경할 수 없고 객체가 제공하는 필드와 메소드를 통해서만 접근이 가능하도록
* 인터페이스를 통한 구현 은닉
#### Inheritance(상속)
* 코드의 재사용성과 유지보수를 위해 사용
#### Polymorphism(다형성)
* Compile time: 정적 바인딩, 메소드 오버로딩
* Runtime: 동적 바인딩, 메소드 오버라이딩

## SOLID 설계 원칙
#### SRP (Single Responsibility Principle)
* 클래스(객체)는 단 하나의 책임(기능)만 가져야 한다
#### OCP (Open Closed Principle)
* 확장에 열려있어야 하며, 수정에는 닫혀있어야 한다 -> 추상화, 상속 사용 
#### LSP (Listov Substitution Principle)
* 서브 타입은 언제나 기반(부모) 타입으로 교체할 수 있어야 한다
* 부모 타입으로 메서드를 실행해도 의도대로 실행되어야 함 -> 다형성
#### ISP (Interface Segregation Principle)
* 인터페이스의 단일 책임 강조
#### DIP (Dependency Inversion Principle)
* 어떤 Class를 참조해서 사용해야하는 상황이 생긴다면, 그 Class를 직접 참조하는 것이 아니라 그 대상의 상위 요소(추상 클래스 or 인터페이스)로 참조해라
* 의존 관계를 맺을 때 변화하기 어려운 것 거의 변화가 없는 것에 의존해라 -> 각 클래스간의 결합도를 낮추기 위함

***
## Reference
- [객체 지향 설계의 5가지 원칙 - S.O.L.I.D](https://inpa.tistory.com/entry/OOP-%F0%9F%92%A0-%EA%B0%9D%EC%B2%B4-%EC%A7%80%ED%96%A5-%EC%84%A4%EA%B3%84%EC%9D%98-5%EA%B0%80%EC%A7%80-%EC%9B%90%EC%B9%99-SOLID)