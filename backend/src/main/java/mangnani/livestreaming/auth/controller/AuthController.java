package mangnani.livestreaming.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangnani.livestreaming.auth.dto.request.LoginRequest;
import mangnani.livestreaming.auth.dto.request.SignUpRequest;
import mangnani.livestreaming.auth.dto.response.LoginResponse;
import mangnani.livestreaming.auth.dto.response.LogoutResponse;
import mangnani.livestreaming.auth.dto.response.SignUpResponse;
import mangnani.livestreaming.auth.service.AuthService;
import mangnani.livestreaming.global.jwt.extractor.TokenExtractor;
import mangnani.livestreaming.global.jwt.filter.JwtAuthenticationFilter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final AuthService authService;
	private final TokenExtractor tokenExtractor;

	@PostMapping("/signup")
	public ResponseEntity<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
		return authService.signUp(signUpRequest);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}

	@GetMapping("/logout")
	public ResponseEntity<LogoutResponse> logout(@AuthenticationPrincipal User user, HttpServletRequest request) {
		String accessToken = tokenExtractor.resolveAccessToken(request);
		return authService.logout(user.getUsername(),accessToken);
	}

	@PostMapping("/reissue")
	public ResponseEntity<String> reissue(HttpServletRequest request) {
		String accessToken = tokenExtractor.resolveAccessToken(request);
		String refreshToken = tokenExtractor.resolveRefreshToken(request);
		return authService.reissue(accessToken, refreshToken);
	}

}
