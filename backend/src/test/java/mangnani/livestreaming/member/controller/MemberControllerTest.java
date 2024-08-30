package mangnani.livestreaming.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import mangnani.livestreaming.member.dto.request.ChangeNicknameRequest;
import mangnani.livestreaming.member.dto.request.ChangePasswordRequest;
import mangnani.livestreaming.member.dto.request.ChangeProfileImage;
import mangnani.livestreaming.member.dto.response.*;
import mangnani.livestreaming.member.service.MemberService;
import mangnani.livestreaming.global.config.SpringSecurityConfig;
import mangnani.livestreaming.auth.jwt.filter.JwtAuthenticationFilter;
import mangnani.livestreaming.auth.jwt.provider.JwtTokenProvider;
import mangnani.livestreaming.auth.jwt.extractor.TokenExtractor;
import mangnani.livestreaming.global.config.CustomUserDetailsService;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import({SpringSecurityConfig.class, JwtAuthenticationFilter.class, JwtTokenProvider.class, TokenExtractor.class})
class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MemberService memberService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@MockBean
	private CustomUserDetailsService userDetailsService;

	@MockBean
	private TokenExtractor tokenExtractor;
	private String validToken;

	@BeforeEach
	void setUp() {
		Authentication auth = new UsernamePasswordAuthenticationToken("testUser", null,
				AuthorityUtils.createAuthorityList("ROLE_USER"));
		validToken = jwtTokenProvider.generateAccessToken(auth);

		when(tokenExtractor.resolveAccessToken(any())).thenReturn(validToken);
		when(userDetailsService.loadUserByUsername(anyString()))
				.thenReturn(new org.springframework.security.core.userdetails.User("testUser", "", AuthorityUtils.createAuthorityList("ROLE_USER")));
	}

	@Test
	@DisplayName("로그인한 회원 정보 조회 - 성공")
	void getSignMember_Success() throws Exception {
		GetSignInMemberResponseDto responseDto = GetSignInMemberResponseDto.success("testUser", "TestNickname", "streamKey123");
		when(memberService.getSignInUser(anyString())).thenReturn(ResponseEntity.ok(responseDto));

		mockMvc.perform(get("/member")
						.header("Authorization", "Bearer " + validToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value("testUser"))
				.andExpect(jsonPath("$.nickname").value("TestNickname"))
				.andExpect(jsonPath("$.streamKey").value("streamKey123"));
	}

	@Test
	@DisplayName("잘못된 토큰으로 회원 정보 조회 - 실패")
	void getSignMember_InvalidToken() throws Exception {
		when(tokenExtractor.resolveAccessToken(any())).thenReturn("invalid-token");

		mockMvc.perform(get("/member")
						.header("Authorization", "Bearer invalid-token"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("회원의 스타캔디 금액 조회 - 성공")
	void getStarCandyAmount_Success() throws Exception {
		GetStarCandyAmountResponse response = GetStarCandyAmountResponse.success("TestNickname", 100);
		when(memberService.getStarCandyAmount(anyString())).thenReturn(ResponseEntity.ok(response));

		mockMvc.perform(get("/member/starcandy")
						.header("Authorization", "Bearer " + validToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nickname").value("TestNickname"))
				.andExpect(jsonPath("$.starCandyAmount").value(100));
	}

	@Test
	@DisplayName("특정 회원 정보 조회 - 성공")
	void getMember_Success() throws Exception {
		GetMemberResponse response = GetMemberResponse.success("testUser", "TestNickname", "profile.jpg");
		when(memberService.getMember(anyString())).thenReturn(ResponseEntity.ok(response));

		mockMvc.perform(get("/member/testUser")
						.header("Authorization", "Bearer " + validToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value("testUser"))
				.andExpect(jsonPath("$.nickname").value("TestNickname"))
				.andExpect(jsonPath("$.profileImage").value("profile.jpg"));
	}

	@Test
	@DisplayName("닉네임 사용 가능 여부 확인 - 성공")
	void checkNicknameAvailability_Success() throws Exception {
		CheckNicknameAvailabilityResponse response = CheckNicknameAvailabilityResponse.success();
		when(memberService.checkNicknameAvailability(anyString())).thenReturn(ResponseEntity.ok(response));

		mockMvc.perform(get("/member/TestNickname/availability")
						.header("Authorization", "Bearer " + validToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("SU"))
				.andExpect(jsonPath("$.message").value("성공"));
	}

	@Test
	@DisplayName("닉네임 변경 - 성공")
	void changeNickname_Success() throws Exception {
		ChangeNicknameRequest request = new ChangeNicknameRequest();
		request.setNewNickname("ChangeNewName");

		ChangeNicknameResponse response = ChangeNicknameResponse.success();
		when(memberService.changeNickname(any(ChangeNicknameRequest.class), anyString())).thenReturn(ResponseEntity.ok(response));

		mockMvc.perform(patch("/member/nickname")
						.header("Authorization", "Bearer " + validToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("SU"))
				.andExpect(jsonPath("$.message").value("성공"));
	}

	@Test
	@DisplayName("비밀번호 변경 - 성공")
	void changePassword_Success() throws Exception {
		ChangePasswordRequest request = new ChangePasswordRequest();
		request.setCurrentPassword("oldPassword@");
		request.setNewPassword("newPassword@");

		ChangePasswordResponse response = ChangePasswordResponse.success();
		when(memberService.changePassword(any(ChangePasswordRequest.class), anyString())).thenReturn(ResponseEntity.ok(response));

		mockMvc.perform(patch("/member/password")
						.header("Authorization", "Bearer " + validToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("SU"))
				.andExpect(jsonPath("$.message").value("성공"));
	}

	@Test
	@DisplayName("프로필 이미지 변경 - 성공")
	void changeProfileImage_Success() throws Exception {
		ChangeProfileImage request = new ChangeProfileImage();
		request.setImage("newImage.jpg");
		ChangeProfileImageResponse response = ChangeProfileImageResponse.success();
		when(memberService.changeProfileImage(anyString(), any(ChangeProfileImage.class))).thenReturn(ResponseEntity.ok(response));

		mockMvc.perform(patch("/member/testUser/profileImage")
						.header("Authorization", "Bearer " + validToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("SU"))
				.andExpect(jsonPath("$.message").value("Success"));
	}

}