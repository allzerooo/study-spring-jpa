package study.springjpa.api;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.springjpa.domain.Member;
import study.springjpa.domain.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

	private final MemberService memberService;

	@GetMapping("/api/v1/members")
	public Result membersV1() {
		final List<Member> members = memberService.findMembers();

		final List<MemberResponse> collect = members.stream()
				.map(m -> new MemberResponse(m.getName()))
				.collect(Collectors.toList());

		return new Result(collect);
	}

	@PostMapping("/api/v1/members")
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid CreateMemberRequest request) {

		Member member = new Member();
		member.setName(request.getName());

		final Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}

	@PutMapping("/api/v1/members/{id}")
	public UpdateMemberResponse updateMemberV1(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {
		memberService.update(id, request.getName());
		Member findMember = memberService.findOne(id);
		return new UpdateMemberResponse(findMember.getId(), findMember.getName());
	}

	@Data
	@AllArgsConstructor
	static class Result<T> {
		private T data;
	}

	@Data
	@AllArgsConstructor
	static class MemberResponse {
		private String name;
	}

	@Data
	static class CreateMemberRequest {
		@NotEmpty
		private String name;
	}

	@Data
	static class CreateMemberResponse {
		private Long id;

		public CreateMemberResponse(final Long id) {
			this.id = id;
		}
	}

	@Data
	static class UpdateMemberRequest {
		private String name;
	}

	@Data
	@AllArgsConstructor
	static class UpdateMemberResponse {
		private Long id;
		private String name;
	}


}
