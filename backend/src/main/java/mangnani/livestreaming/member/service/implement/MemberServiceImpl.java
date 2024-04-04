package mangnani.livestreaming.member.service.implement;

import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import mangnani.livestreaming.member.service.MemberService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberRepository memberRepository;

	@Override
	public Member findByLoginId(String loginId) {
		return memberRepository.findByLoginId(loginId).orElseThrow(NoExistedMember::new);
	}
}
