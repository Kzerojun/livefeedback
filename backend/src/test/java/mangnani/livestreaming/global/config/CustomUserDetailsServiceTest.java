package mangnani.livestreaming.global.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mangnani.livestreaming.auth.exception.LoginFailedException;
import mangnani.livestreaming.member.constant.Role;
import mangnani.livestreaming.member.constant.Status;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

	@Mock
	MemberRepository memberRepository;

	@InjectMocks
	CustomUserDetailsService customUserDetailsService;


	@Test
	@DisplayName("사용자가 존재하면 UserDetails를 반환한다")
	void loadUser_Success() {

		String loginId = "testUser";
		Member member = Member.builder()
				.loginId(loginId)
				.password("testpwd")
				.status(Status.ACTIVATE)
				.role(Role.USER)
				.nickname("testNickname")
				.build();

		when(memberRepository.findByLoginId(loginId)).thenReturn(Optional.of(member));

		UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);

		assertThat(userDetails).isNotNull();
		assertThat(userDetails.getUsername()).isEqualTo(loginId);
		assertThat(userDetails.getPassword()).isEqualTo(member.getPassword());
		assertThat(userDetails.getAuthorities()).hasSize(1);
		assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo(Role.USER.toString());
	}

	@Test
	@DisplayName("사용자가 존재하지 않으면 LoginFailedException을 던진다")
	public void loadUser_Fail() {
		// 준비 (Arrange)
		String loginId = "nonExistentUser";
		when(memberRepository.findByLoginId(loginId)).thenReturn(Optional.empty());

		// 실행 및 검증 (Act & Assert)
		assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(loginId))
				.isInstanceOf(LoginFailedException.class);
	}

}