
`@Repository`
- 컴포넌트 스캔의 대상으로, Spring이 bean으로 등록해줌
- Spring Boot는 기본적으로 `@SpringBootApplication`이 있는 클래스의 패키지와 하위 패키지를 모두 컴포넌트 스캔해 bean으로 자동 등록한다

`@PersistenceUnit`
- EntityMangerFactory를 주입받고 싶을 때 사용

`@PersistenceContext`
- JPA가 제공하는 표준 어노테이션
- Spring이 EntityManager를 만들어서 주입해준다