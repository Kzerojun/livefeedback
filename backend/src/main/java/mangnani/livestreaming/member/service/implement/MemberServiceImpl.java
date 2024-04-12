package mangnani.livestreaming.member.service.implement;

import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.member.dto.response.GetSignInMemberResponseDto;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import mangnani.livestreaming.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;

	@Override
	public ResponseEntity<GetSignInMemberResponseDto> getSignInUser(String loginId) {
		Member member = memberRepository.findByLoginId(loginId).orElseThrow(NoExistedMember::new);
		return ResponseEntity.ok().body(GetSignInMemberResponseDto.success(member.getLoginId(),
				member.getNickname()));
	}
}
