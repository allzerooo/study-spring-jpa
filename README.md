
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

`@AllArgsConstructor`
- 모든 필드의 생성자를 추가해준다

`@RequiredArgsContructor`
- final 필드만 가지는 생성자를 추가해준다

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