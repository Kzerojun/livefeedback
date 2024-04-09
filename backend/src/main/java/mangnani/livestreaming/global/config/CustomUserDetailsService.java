package mangnani.livestreaming.global.config;

import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangnani.livestreaming.auth.exception.LoginFailedException;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		return memberRepository.findByLoginId(loginId)
				.map(this::createUserDetails)
				.orElseThrow(LoginFailedException::new);
	}

	private UserDetails createUserDetails(Member member) {
		Collection<? extends GrantedAuthority> authorities = getAuthorities(member);
		return new User(member.getLoginId(), member.getPassword(), authorities);
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Member member) {
		return Collections.singletonList(
				new SimpleGrantedAuthority("ROLE_" + member.getRole().name()));
	}
}
