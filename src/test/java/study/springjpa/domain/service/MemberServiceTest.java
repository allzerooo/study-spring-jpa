package study.springjpa.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.springjpa.domain.Member;
import study.springjpa.repository.MemberRepository;

@SpringBootTest
@Transactional
class MemberServiceTest {

	@Autowired MemberService memberService;
	@Autowired MemberRepository memberRepository;

	@Test
	void 회원가입() {
	  // given
		Member member = new Member();
		member.setName("kim");

	  // when
		final Long savedId = memberService.join(member);

		// then
		assertEquals(member, memberRepository.findOne(savedId));  // @Transactional이 있기 때문에 같은 트랜잭션이면 JPA는 ID 값이 똑같으면 같은 Entity로 관리가 된다;
	}

	@Test
	void 중복_회원_예외() {
	  // given
		Member member1 = new Member();
		member1.setName("kim");

		Member member2 = new Member();
		member2.setName("kim");

	  // when
		memberService.join(member1);
		assertThrows(IllegalStateException.class, () -> {
			memberService.join(member2);
		});
	}
}