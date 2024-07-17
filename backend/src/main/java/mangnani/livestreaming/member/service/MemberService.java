package mangnani.livestreaming.member.service;


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
import org.springframework.http.ResponseEntity;

public interface MemberService {

	ResponseEntity<GetSignInMemberResponseDto> getSignInUser(String userId);

	ResponseEntity<GetStarCandyAmountResponse> getStarCandyAmount(String userId);

	ResponseEntity<GetMemberResponse> getMember(String userId);

	ResponseEntity<CheckNicknameAvailabilityResponse> checkNicknameAvailability(String nickname);

	ResponseEntity<ChangeNicknameResponse> changeNickname(ChangeNicknameRequest request, String userId);

	ResponseEntity<ChangePasswordResponse> changePassword(ChangePasswordRequest request,
			String userId);

	ResponseEntity<ChangeProfileImageResponse> changeProfileImage(String userId, ChangeProfileImage image);

}
