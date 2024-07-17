package mangnani.livestreaming.member.controller;

import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.UnauthorizedException;
import mangnani.livestreaming.member.dto.request.ChangeNicknameRequest;
import mangnani.livestreaming.member.dto.request.ChangePasswordRequest;
import mangnani.livestreaming.member.dto.request.ChangeProfileImage;
import mangnani.livestreaming.member.dto.response.ChangeNicknameResponse;
import mangnani.livestreaming.member.dto.response.ChangePasswordResponse;
import mangnani.livestreaming.member.dto.response.ChangeProfileImageResponse;
import mangnani.livestreaming.member.dto.response.CheckNicknameAvailabilityResponse;
import mangnani.livestreaming.member.dto.response.GetMemberResponse;
import mangnani.livestreaming.member.dto.response.GetSignInMemberResponseDto;
import mangnani.livestreaming.member.dto.response.GetStarCandyAmountResponse;
import mangnani.livestreaming.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

		if (user == null || user.getUsername() == null) {
			throw new UnauthorizedException(ResponseCode.UNAUTHORIZED_KEY,
					ResponseMessage.UNAUTHORIZED_KEY);
		}

		return memberService.getSignInUser(user.getUsername());
	}

	@GetMapping("/starcandy")
	public ResponseEntity<GetStarCandyAmountResponse> getStarCandyAmount(
			@AuthenticationPrincipal User user) {
		return memberService.getStarCandyAmount(user.getUsername());
	}

	@GetMapping("/{userId}")
	public ResponseEntity<GetMemberResponse> getMember(@PathVariable String userId) {
		return memberService.getMember(userId);
	}

	@GetMapping("/{nickname}/availability")
	public ResponseEntity<CheckNicknameAvailabilityResponse> checkNicknameAvailability(
			@PathVariable String nickname) {
		return memberService.checkNicknameAvailability(nickname);
	}

	@PatchMapping("/nickname")
	public ResponseEntity<ChangeNicknameResponse> changeNickname(@AuthenticationPrincipal User user,
			@RequestBody ChangeNicknameRequest request) {

		return memberService.changeNickname(request,user.getUsername());
	}

	@PatchMapping("/password")
	public ResponseEntity<ChangePasswordResponse> changePassword(@AuthenticationPrincipal User user,
			@RequestBody ChangePasswordRequest request){
		return memberService.changePassword(request, user.getUsername());
	}

	@PatchMapping("/{userId}/profileImage")
	public ResponseEntity<ChangeProfileImageResponse> changeProfileImage(
			@AuthenticationPrincipal User user, @RequestBody ChangeProfileImage image) {
		return memberService.changeProfileImage(user.getUsername(), image);
	}
}
