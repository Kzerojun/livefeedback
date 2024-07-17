package mangnani.livestreaming.member.service.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mangnani.livestreaming.member.dto.request.ChangeNicknameRequest;
import mangnani.livestreaming.member.dto.request.ChangePasswordRequest;
import mangnani.livestreaming.member.dto.response.ChangePasswordResponse;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.InvalidPasswordException;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import mangnani.livestreaming.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

	@Mock
	MemberRepository memberRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	MemberServiceImpl memberService;

	@Nested
	class ChangePasswordTests {

		private Member member;

		private ChangePasswordRequest passwordRequest;

		@BeforeEach
		void setUp() {
			// 비밀번호 변경 요청 및 회원 설정
			 passwordRequest = new ChangePasswordRequest();
			passwordRequest.setCurrentPassword("oldPassword");
			passwordRequest.setNewPassword("newPassword");

			 member = Member.builder()
					.loginId("test1234")
					.password("encodedOldPassword")
					.nickname("테스터")
					.build();
		}

		@Test
		@DisplayName("비밀번호 변경 - 성공")
		void changePassword_success() {
			// Given
			when(memberRepository.findByLoginId("userId")).thenReturn(Optional.of(member));
			when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);
			when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

			// When
			ResponseEntity<ChangePasswordResponse> response = memberService.changePassword(passwordRequest, "userId");

			// Then
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
			verify(memberRepository).findByLoginId("userId");
			verify(passwordEncoder).matches("oldPassword", "encodedOldPassword");
			verify(passwordEncoder).encode("newPassword");

		}

		@Test
		@DisplayName("비밀번호 변경 - 실패 (멤버 존재 X)")
		void changePassword_memberNotFound() {
			// Given
			when(memberRepository.findByLoginId("userId")).thenReturn(Optional.empty());

			// When / Then
			assertThrows(NoExistedMember.class, () -> memberService.changePassword(passwordRequest, "userId"));
			verify(memberRepository).findByLoginId("userId");
		}

		@Test
		@DisplayName("비밀번호 변경 - 실패 (현 비밀번호가 올바르지 않음)")
		void changePassword_invalidPassword() {
			// Given
			when(memberRepository.findByLoginId("userId")).thenReturn(Optional.of(member));
			when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(false);

			// When / Then
			assertThrows(InvalidPasswordException.class, () -> memberService.changePassword(passwordRequest, "userId"));
			verify(memberRepository).findByLoginId("userId");
			verify(passwordEncoder).matches("oldPassword", "encodedOldPassword");
		}
	}



}