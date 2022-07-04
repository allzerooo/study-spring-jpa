package study.springjpa.domain.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.springjpa.domain.Member;
import study.springjpa.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	/**
	 * 회원 가입
	 */
	@Transactional
	public Long join(Member member) {

		// 중복 회원 검증
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

	/**
	 * 회원 조회
	 */
	public Member findOne(Long memberId) {
		return memberRepository.findOne(memberId);
	}

	/**
	 * 회원 전체 조회
	 */
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	@Transactional
	public void update(final Long id, final String name) {
		final Member member = memberRepository.findOne(id);
		member.setName(name);
	}
}
