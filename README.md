
`@Repository`
- 컴포넌트 스캔의 대상으로, Spring이 bean으로 등록해줌
- Spring Boot는 기본적으로 `@SpringBootApplication`이 있는 클래스의 패키지와 하위 패키지를 모두 컴포넌트 스캔해 bean으로 자동 등록한다

`@PersistenceUnit`
- EntityMangerFactory를 주입받고 싶을 때 사용

`@PersistenceContext`
- JPA가 제공하는 표준 어노테이션
- Spring이 EntityManager를 만들어서 주입해준다

`@Transactinal`
- JPA의 데이터 접근 로직들은 가급적 트랜잭션 안에서 실행되어야 한다. 그래야 Lazy Loading 이런 것들이 된다
- 클래스 레벨에 `@Transactinal`이 있으면 `public` 메서드들은 모두 적용이 된다
- Spring이 제공하는 `@Transactinal` 어노테이션과 javax가 제공하는 `@Transactinal` 어노테이션이 있는데 Spring 안에서는 Spring이 제공하는 것을 사용해야 쓸 수 있는 옵션들이 많다
- `readOnly = true` 옵션을 주면 조회하는 로직에서는 JPA가 좀 더 성능을 최적화 한다
  - 읽기 메서드가 많은 클래스에는 클래스 레벨로 `@Transactinal(readOnly = true)`를 사용하고, 쓰기가 있는 메서드에 따로 `@Transactinal`을 추가해도 된다(메서드에 설정되어 있는게 우선권을 가짐)
- 테스트 클래스에 설정되어 있으면 롤백이 된다 → 원하지 않을 경우 `@Rollback(false)`를 추가하면 된다

`@AllArgsConstructor`
- 모든 필드의 생성자를 추가해준다

`@RequiredArgsContructor`
- final 필드만 가지는 생성자를 추가해준다

`CascadeType`
- `CascadeType.ALL`
  - private owner인 경우에 사용하면 좋다. 해당 객체를 다른 곳에서 참조하지 않고 private owner와 라이프사이클을 같이할 때

### 1차 캐시의 key
```java
@Transactional
public Long join(Member member) {
    memberRepository.save(member);
    return member.getId();
}
```
- `em.persist`를 할 때 DB에 들간 시점이 아니어도 1차 캐시의 key로 id(PK) 값이 있도록 보장해주기 때문에 `member.getId()`가 가능하다

### DB 필드에 유니크 제약 조건이 필요한 경우
```java
@Transactional
public Long join(Member member) {

    validateDuplicateMember(member);
    memberRepository.save(member);
    return member.getId();  // em.persist를 할 때 DB에 들간 시점이 아니어도 id(PK) 값이 있도록 보장해줌
}

private void validateDuplicateMember(final Member member) {
    final List<Member> findMembers = memberRepository.findByName(member.getName());
    if (!findMembers.isEmpty()) {
        throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
}
```
- 멀티쓰레드 환경에서는 n개의 쓰레드가 동시에 DB에 접근해서 validation 체크를 하고, 저장하려고 할 수 있다. 따라서 안전하게 DB에도 유니크 제약 조건을 주는게 좋다

### EntityManager 주입
Spring Data JPA를 사용하면 
```java
@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;
}
```
이렇게 주입해줬던 것을
```java
@Repository
@RequiredArgsConstructor
public class MemberRepository {

  private final EntityManager em;
}
```
이렇게 변경할 수 있다

### test > resources > application.yml
- 테스트 디렉토리 안에서 우선권을 가짐

### 메모리 DB 사용
- 별도의 DB(h2와 같은)를 띄우지 않고 Spring이 뜰 때 사용할 수 있는 DB
- build.gradle에 `com.h2database:h2` 의존성이 있으면 메모리 모드로 JVM 안에서 띄울 수 있다
- application.yml의 `spring.datasource.url`을 `jdbc:h2:mem:test`로 바꿔주면 된다
- Spring Boot에서는 `spring.datasource`, `spring.jpa` 설정이 다 없어도 동작한다 → 별도의 설정이 없으면 메모리 모드로 실행하기 때문에
- Srping Boot는 `sping.jpa.hibernate.ddl-auto: create-drop`이 기본 설정이다

### Entity 생성 제한하기
- JPA는 `protected`까지 기본 생성자를 만들 수 있도록 허용해준다
- `protected` 기본 생성자를 추가해두면 다른 곳에서 생성 메서드를 사용하지 않고 Entity를 생성하지 못하도록 제한할 수 있다
- Lombok으로 변경 가능하다 → `@NoArgsConstructor(access = AccessLevel.PROTECTED`

### 도메인 모델 패턴
- 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것
- 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할을 한다

### 트랜잭션 스크립트 패턴
- 엔티티에는 비즈니스 로직이 거의 없고 서비스 계청에서 대부분의 비즈니스 로직을 처리하는 것

### JPA에서 동적 쿼리를 어떻게 해결해야 하는가?
- JPQL 쿼리를 String으로 조합하거나, Criteria는 실무에서 사용하기 어렵다
- QueryDsl

### Log
```java
Logger log = LoggerFactory.getLogger(getClass();)
```
이렇게 Logger 객체를 생성해서 사용하는데, Lombok의 `@Sl4fj` annotation으로 변경할 수 있다

### Entity를 최대한 순수하게 유지
- 화면과 관련된 기능은 DTO와 같은 다른 클래스에서 처리
- Entity를 절대 API의Reponse로 넘기면 안된다
  - API는 스펙인데 Entity가 변경되면 API도 변경되기 때문이다

### 변경 감지와 병합(merge)
준영속 엔티티 : 영속성 컨텍스트가 더는 관리하지 않는 엔티티를 말한다.
```java
Book book = new Book();
book.setId(form.getId());
```
`Book` 객체는 이미 DB에 한번 저장되어서 JPA가 식별할 수 있는 ID가 존재한다. 임으로 만들어낸 엔티티도 식별자를 가지고 있으면 준영속 엔티티로 볼 수 있다

준영속 엔티티는 JPA가 관리를 안하기 때문에 변경 감지가 되지 않는다. 따라서 DB에 업데이트가 일어나지 않는다.
준영속 엔티티를 수정하는 방법
- 변경 감지 기능 사용
  ```java
  @Transactional
  public Item update(Item param) { // param : 파라미터로 넘어온 준영속 상태의 엔티티
    Item findItem = em.find(Item.class, param.getId()); // 같은 엔티티를 조회한다
    findItem.setPrice(param.getPrice());  // JPA가 관리하는 엔티티를 수정
    return findItem;
  }
  ```
- 병합(`merge`) 사용
  - 준영속 상태의 엔티티를 영속 상태로 변경할 때 사용하는 기능
  - 위 변경 감지 기능과 동일하게 작동한다. 즉, 위 코드를 JPA가 한줄로 해결해주는 것이다
  - 자세한 동작 방식
    - `merge()`를 실행한다
    - 파라미터로 넘아온 준영속 엔티티의 식별자 값으로 1차 캐시에서 엔티티를 조회한다
      - 1차 캐시에 엔티티가 없으면 데이터베이스에서 엔티티를 조회하고, 1차 캐시에 저장한다
    - 조회한 영속 엔티티에 준영속 엔티티의 값을 채워 넣는다 (준영속 엔티티의 모든 값)
    - 영속 상태인 엔티티를 반환한다
  - `em.merge(book)`에 보낸 `book`의 식별자로 DB에서 조회 후 `book`에 있는 값들로 변경
  - 주의 : 변경 감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있지만, 병합을 사용하면 모든 속성이 변경된다. 병합 시 값이 없으면 null로 업데이트 할 위험도 있다
    - 가급적 `merge`를 쓰지 말고, 변경 감지를 사용해 필요한 필드를 업데이트 해주는게 좋다

### 컨트롤러에서 애매하게 Entity를 생성하지 말기
- 트랜잭션이 있는 서비스 계층에 식별자와 변경할 데이터를 명확하게 전달(파라미터 or DTO)
  - Web 계층에서 받아온 값으로 컨트롤러에서 Entity를 생성하는 것보다, Web 계층에서 받은 값 중 Service 계층에서 필요로하는 값을 전달해 Service 계층에서 Entity를 다루는 것이 좋다
  - 서비스 계층에서 받아야 할 데이터가 많다면 서비스 계층에 DTO를 생성
- 트랜잭션이 있는 서비스 계층에서 영속 상태의 엔티티를 조회하고, 엔티티의 데이터를 직접 변경
- 트랜잭션 커밋 시점에 변경 감지가 실행