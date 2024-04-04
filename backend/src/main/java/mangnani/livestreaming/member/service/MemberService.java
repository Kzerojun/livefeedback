package mangnani.livestreaming.member.service;


import mangnani.livestreaming.member.entity.Member;

public interface MemberService {

	Member findByLoginId(String loginId);

}
