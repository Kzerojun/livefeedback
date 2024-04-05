package mangnani.livestreaming.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangnani.livestreaming.auth.dto.request.LogoutRequest;
import mangnani.livestreaming.auth.dto.request.ReissueRequest;
import mangnani.livestreaming.auth.dto.request.LoginRequest;
import mangnani.livestreaming.auth.dto.request.SignUpRequest;
import mangnani.livestreaming.auth.dto.response.LoginResponse;
import mangnani.livestreaming.auth.dto.response.SignUpResponse;
import mangnani.livestreaming.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<SignUpResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
		return authService.signUp(signUpRequest);
	}

	@PostMapping("/signin")
	public ResponseEntity<LoginResponse> signIn(@RequestBody @Valid LoginRequest loginRequest) {
		return authService.signIn(loginRequest);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@RequestBody @Valid LogoutRequest logoutRequest) {
		return authService.logout(logoutRequest);
	}

	//TODO
	@PostMapping("/reissue")
	public ResponseEntity<?> reissue(@RequestBody @Valid ReissueRequest reissueRequest) {
		return authService.reissue(reissueRequest);
	}
}
