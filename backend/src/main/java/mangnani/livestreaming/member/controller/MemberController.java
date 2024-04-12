package mangnani.livestreaming.member.controller;

import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.member.dto.response.GetSignInMemberResponseDto;
import mangnani.livestreaming.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping
	public ResponseEntity<GetSignInMemberResponseDto> getSignMember(
			@AuthenticationPrincipal User user) {
		return memberService.getSignInUser(user.getUsername());
	}
}
