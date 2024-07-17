package mangnani.livestreaming.auth.controller;

import static mangnani.livestreaming.utils.ApiDocumentUtils.getDocumentRequest;
import static mangnani.livestreaming.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.fasterxml.jackson.databind.ObjectMapper;
import mangnani.livestreaming.auth.dto.request.LoginRequest;
import mangnani.livestreaming.auth.dto.request.SignUpRequest;
import mangnani.livestreaming.auth.dto.response.LoginResponse;
import mangnani.livestreaming.auth.dto.response.SignUpResponse;
import mangnani.livestreaming.auth.exception.DuplicatedLoginIdException;
import mangnani.livestreaming.auth.exception.DuplicatedNicknameException;
import mangnani.livestreaming.auth.service.AuthService;
import mangnani.livestreaming.global.config.SpringSecurityConfig;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.auth.jwt.extractor.TokenExtractor;
import mangnani.livestreaming.auth.jwt.filter.JwtAuthenticationFilter;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.utils.MockMvcHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(controllers = AuthController.class, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SpringSecurityConfig.class)})
// (1)
@MockBean(JpaMetamodelMappingContext.class)   // (2)
@AutoConfigureRestDocs
class AuthControllerTest {

	@RegisterExtension
	final RestDocumentationExtension restDocumentation = new RestDocumentationExtension(
			"build/generated-snippets");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvcHelper mockMvcHelper;

	@MockBean
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@MockBean
	private TokenExtractor tokenExtractor;

	@MockBean
	private AuthService authService;

	private final String AUTH_SIGNUP_URL = "/auth/signup";
	private final String AUTH_LOGIN_URL = "/auth/login";
	private final String AUTH_LOGOUT_URL = "/auth/logout";
	private final String AUTH_REISSUE_URL = "/auth/reissue";

	private final String ACCESS_TOKEN = "accessToken";
	private final String REFRESH_TOKEN = "refreshToken";
	@BeforeEach
	void setUp(
			WebApplicationContext webApplicationContext,
			RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(restDocumentation))
				.build();
		mockMvcHelper = new MockMvcHelper(mockMvc, objectMapper);
	}

	@Test
	@DisplayName("회원가입 성공")
	void signUp_success() throws Exception {
		//given
		SignUpResponse success = SignUpResponse.success();
		SignUpRequest request = signUpRequest();


		//when
		when(authService.signUp(any(SignUpRequest.class))).thenReturn(
				ResponseEntity.ok().body(success));
		ResultActions result = this.mockMvc.perform(post(AUTH_SIGNUP_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.accept(MediaType.APPLICATION_JSON)
		);

		//then
		result.andExpect(status().isOk())
				.andDo(document("auth/signup/success/",
						getDocumentRequest(),
						getDocumentResponse(),
						requestFields(
								fieldWithPath("loginId").type(JsonFieldType.STRING)
										.description("로그인 아이디"),
								fieldWithPath("password").type(JsonFieldType.STRING)
										.description("패스워드"),
								fieldWithPath("nickname").type(JsonFieldType.STRING)
										.description("닉네임")
						),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING)
										.description("결과 코드"),
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("결과 메시지")
						)
				));
	}

	@Test
	@DisplayName("회원가입 실패 - loginId 패턴 틀림")
	void singUp_Fail_LoginId() throws Exception {
		//given
		SignUpRequest signUpRequest = signUpRequest();
		signUpRequest.setLoginId(" ");

		ResultActions result = this.mockMvc.perform(post(AUTH_SIGNUP_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest))
				.accept(MediaType.APPLICATION_JSON)
		);

		result.andExpect(status().isBadRequest())
				.andDo(document("auth/signup/fail/loginId",
						getDocumentRequest(),
						getDocumentResponse(),
						requestFields(
								fieldWithPath("loginId").type(JsonFieldType.STRING)
										.description("잘못된 로그인 아이디"),
								fieldWithPath("password").type(JsonFieldType.STRING)
										.description("패스워드"),
								fieldWithPath("nickname").type(JsonFieldType.STRING)
										.description("닉네임")
						),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING)
										.description("결과 코드"),
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("결과 메시지")
						)));

	}

	@Test
	@DisplayName("회원가입 실패 - nickname 패턴 틀림")
	void signUp_fail_nickname() throws Exception {
		//given
		SignUpRequest signUpRequest = signUpRequest();
		signUpRequest.setNickname(" ");

		ResultActions result = this.mockMvc.perform(post(AUTH_SIGNUP_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest))
				.accept(MediaType.APPLICATION_JSON)
		);

		result.andExpect(status().isBadRequest())
				.andDo(document("auth/signup/fail/nickname",
						getDocumentRequest(),
						getDocumentResponse(),
						requestFields(
								fieldWithPath("loginId").type(JsonFieldType.STRING)
										.description("로그인 아이디"),
								fieldWithPath("password").type(JsonFieldType.STRING)
										.description("패스워드"),
								fieldWithPath("nickname").type(JsonFieldType.STRING)
										.description("잘못된 닉네임")
						),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING)
										.description("결과 코드"),
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("결과 메시지")
						)));
	}

	@Test
	@DisplayName("회원가입 실패 - 패스워드 패턴 틀림")
	void signUp_fail_password() throws Exception {
		SignUpRequest signUpRequest = signUpRequest();
		signUpRequest.setPassword(" ");

		ResultActions result = this.mockMvc.perform(post(AUTH_SIGNUP_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest))
				.accept(MediaType.APPLICATION_JSON)
		);

		result.andExpect(status().isBadRequest())
				.andDo(document("auth/signup/fail/password",
						getDocumentRequest(),
						getDocumentResponse(),
						requestFields(
								fieldWithPath("loginId").type(JsonFieldType.STRING)
										.description("로그인 아이디"),
								fieldWithPath("password").type(JsonFieldType.STRING)
										.description("잘못된 패스워드"),
								fieldWithPath("nickname").type(JsonFieldType.STRING)
										.description("닉네임")
						),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING)
										.description("결과 코드"),
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("결과 메시지")
						)));
	}

	@Test
	@DisplayName("회원가입 실패 - 중복된 로그인 아이디")
	void signUp_fail_duplicate_loginId() throws Exception {
		//given
		SignUpRequest signUpRequest = signUpRequest();

		//when
		when(authService.signUp(any(SignUpRequest.class))).thenThrow(
				new DuplicatedLoginIdException());
		ResultActions result = this.mockMvc.perform(post(AUTH_SIGNUP_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest))
				.accept(MediaType.APPLICATION_JSON)
		);

		result.andExpect(status().isBadRequest())
				.andDo(document("auth/signup/fail/duplicated_loginId",
						getDocumentRequest(),
						getDocumentResponse(),
						requestFields(
								fieldWithPath("loginId").type(JsonFieldType.STRING)
										.description("중복된 로그인 아이디"),
								fieldWithPath("password").type(JsonFieldType.STRING)
										.description("패스워드"),
								fieldWithPath("nickname").type(JsonFieldType.STRING)
										.description("닉네임")
						),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING)
										.description("결과 코드"),
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("결과 메시지")
						)));
	}

	@Test
	@DisplayName("회원가입 실패 - 중복된 닉네임 아이디")
	void signUp_fail_duplicate_nickname() throws Exception {
		//given
		SignUpRequest signUpRequest = signUpRequest();

		//when
		when(authService.signUp(any(SignUpRequest.class))).thenThrow(
				new DuplicatedNicknameException());
		ResultActions result = this.mockMvc.perform(post(AUTH_SIGNUP_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(signUpRequest))
				.accept(MediaType.APPLICATION_JSON)
		);

		result.andExpect(status().isBadRequest())
				.andDo(document("auth/signup/fail/duplicated_nickname",
						getDocumentRequest(),
						getDocumentResponse(),
						requestFields(
								fieldWithPath("loginId").type(JsonFieldType.STRING)
										.description("로그인 아이디"),
								fieldWithPath("password").type(JsonFieldType.STRING)
										.description("패스워드"),
								fieldWithPath("nickname").type(JsonFieldType.STRING)
										.description("중복된 닉네임")
						),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING)
										.description("결과 코드"),
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("결과 메시지")
						)));
	}

	@Test
	@DisplayName("로그인 성공")
	void login_success() throws Exception {
		//given
		LoginRequest loginRequest = loginRequest();
		LoginResponse loginResponse = LoginResponse.builder()
				.refreshToken("refreshToken")
				.accessToken("accessToken")
				.accessTokenExpirationTime(100L)
				.refreshTokenExpirationTime(100L)
				.grantType("Bearer")
				.build();

		when(authService.login(any(LoginRequest.class))).thenReturn(
				ResponseEntity.ok().body(loginResponse));

		//when
		ResultActions result = mockMvcHelper.performPost(AUTH_LOGIN_URL, loginRequest);

		result.andExpect(status().isOk())
				.andDo(document("auth/login/success",
						getDocumentRequest(),
						getDocumentResponse(),
						requestFields(
								fieldWithPath("loginId").type(JsonFieldType.STRING)
										.description("로그인 아이디"),
								fieldWithPath("password").type(JsonFieldType.STRING)
										.description("패스워드")
						),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING)
										.description("결과 코드"),
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("결과 메시지"),
								fieldWithPath("grantType").type(JsonFieldType.STRING)
										.description("grant Type"),
								fieldWithPath("accessToken").type(JsonFieldType.STRING)
										.description("액세스 토큰"),
								fieldWithPath("refreshToken").type(JsonFieldType.STRING)
										.description("리프레쉬 토큰"),
								fieldWithPath("accessTokenExpirationTime").type(
										JsonFieldType.NUMBER).description("액세스 토큰 만료시간"),
								fieldWithPath("refreshTokenExpirationTime").type(
										JsonFieldType.NUMBER).description("리프레쉬 토큰 만료시간")
						)));
	}

	@Test
	@DisplayName("로그인 실패 - 비밀번호 틀림")
	void login_failed_wrongPassword() throws Exception {
		// given
		LoginRequest loginRequest = loginRequest();
		loginRequest.setPassword("wrongPassword");
		when(authService.login(any(LoginRequest.class))).thenThrow(BadCredentialsException.class);

		// Perform the request
		ResultActions result = mockMvcHelper.performPost(AUTH_LOGIN_URL, loginRequest);
		// then
		result.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.code").value(ResponseCode.LOGIN_FAILED))
				.andExpect(jsonPath("$.message").value(ResponseMessage.LOGIN_FAILED))
				.andDo(document("auth/login/fail/invalidPassword",
						getDocumentRequest(),
						getDocumentResponse(),
						requestFields(
								fieldWithPath("loginId").type(JsonFieldType.STRING)
										.description("로그인 아이디"),
								fieldWithPath("password").type(JsonFieldType.STRING)
										.description("잘못된 패스워드")
						),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING)
										.description("결과 코드"),
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("결과 메시지")
						)));
	}

	@Test
	@DisplayName("로그인 실패 - 회원 존재 X")
	void login_failed_noExistedMember() throws Exception{
		//given
		LoginRequest loginRequest = loginRequest();
		when(authService.login(any(LoginRequest.class))).thenThrow(new NoExistedMember());

		//when
		ResultActions result = mockMvcHelper.performPost(AUTH_LOGIN_URL, loginRequest);

		// then
		result.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(ResponseCode.NO_EXISTED_MEMBER))
				.andExpect(jsonPath("$.message").value(ResponseMessage.NO_EXISTED_MEMBER))
				.andDo(document("auth/login/fail/noExistedMember",
						getDocumentRequest(),
						getDocumentResponse(),
						requestFields(
								fieldWithPath("loginId").type(JsonFieldType.STRING)
										.description("로그인 아이디"),
								fieldWithPath("password").type(JsonFieldType.STRING)
										.description("패스워드")
						),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING)
										.description("결과 코드"),
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("결과 메시지")
						)));
	}


	private static LoginRequest loginRequest() {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setLoginId("test1234");
		loginRequest.setPassword("test1234@");
		return loginRequest;
	}

	private static SignUpRequest signUpRequest() {
		SignUpRequest request = new SignUpRequest();
		request.setLoginId("test1234");
		request.setPassword("test1234@");
		request.setNickname("테스터");

		return request;
	}

}