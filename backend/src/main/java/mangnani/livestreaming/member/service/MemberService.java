package mangnani.livestreaming.member.service;


import mangnani.livestreaming.member.dto.response.GetMemberResponse;
import mangnani.livestreaming.member.dto.response.GetSignInMemberResponseDto;
import org.springframework.http.ResponseEntity;

public interface MemberService {

	ResponseEntity<GetSignInMemberResponseDto> getSignInUser(String email);

	ResponseEntity<GetMemberResponse> getMember(String userId);

}
