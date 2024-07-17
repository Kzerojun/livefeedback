package mangnani.livestreaming.member.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.DuplicatedNickname;
import mangnani.livestreaming.member.exception.InvalidPasswordException;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import mangnani.livestreaming.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public ResponseEntity<GetSignInMemberResponseDto> getSignInUser(String loginId) {
		Member member = memberRepository.findByLoginId(loginId).orElseThrow(NoExistedMember::new);
		return ResponseEntity.ok().body(GetSignInMemberResponseDto.success(member.getLoginId(),
				member.getNickname(),member.getStreamKey()));
	}

	@Override
	public ResponseEntity<GetMemberResponse> getMember(String userId) {
		Member member = memberRepository.findByLoginId(userId).orElseThrow(NoExistedMember::new);
		return ResponseEntity.ok()
				.body(GetMemberResponse.success(member.getLoginId(), member.getNickname(),
						member.getProfileImage()));
	}

	@Override
	public ResponseEntity<GetStarCandyAmountResponse> getStarCandyAmount(String userId) {
		Member member = memberRepository.findByLoginId(userId).orElseThrow(NoExistedMember::new);
		return ResponseEntity.ok()
				.body(GetStarCandyAmountResponse.success(member.getNickname(),member.getStarCandyAmount()));
	}


	@Override
	public ResponseEntity<CheckNicknameAvailabilityResponse> checkNicknameAvailability(
			String nickname) {

		checkNicknameDuplicate(nickname);

		return ResponseEntity.ok().body(CheckNicknameAvailabilityResponse.success());
	}

	@Override
	@Transactional
	public ResponseEntity<ChangePasswordResponse> changePassword(ChangePasswordRequest request,
			String userId) {

		Member member = memberRepository.findByLoginId(userId).orElseThrow(NoExistedMember::new);
		boolean matches = passwordEncoder.matches(request.getCurrentPassword(), member.getPassword());

		if (!matches) {
			throw new InvalidPasswordException();
		}

		String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
		member.changePassword(encodedNewPassword);

		return ResponseEntity.ok().body(ChangePasswordResponse.success());
	}

	@Override
	@Transactional
	public ResponseEntity<ChangeNicknameResponse> changeNickname(ChangeNicknameRequest request, String userId) {
		Member member = memberRepository.findByLoginId(userId).orElseThrow(NoExistedMember::new);

		checkNicknameDuplicate(request.getNewNickname());
		member.changeNickname(request.getNewNickname());

		return ResponseEntity.ok().body(ChangeNicknameResponse.success());
	}

	@Override
	@Transactional
	public ResponseEntity<ChangeProfileImageResponse> changeProfileImage(String userId,
			ChangeProfileImage image) {
		Member member = memberRepository.findByLoginId(userId).orElseThrow(NoExistedMember::new);
		member.changeProfileImage(image.getImage());

		return ResponseEntity.ok().body(ChangeProfileImageResponse.success());
	}

	private void checkNicknameDuplicate(String nickname) {
		boolean isDuplicateNickname = memberRepository.existsByNickname(nickname);

		if (isDuplicateNickname) {
			throw new DuplicatedNickname();
		}
	}
}
