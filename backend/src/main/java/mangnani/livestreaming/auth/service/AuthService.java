package mangnani.livestreaming.auth.service;

import mangnani.livestreaming.auth.dto.request.LogoutRequest;
import mangnani.livestreaming.auth.dto.request.ReissueRequest;
import mangnani.livestreaming.auth.dto.request.SignInRequest;
import mangnani.livestreaming.auth.dto.request.SignUpRequest;
import mangnani.livestreaming.auth.dto.response.SignInResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

	ResponseEntity<Void> signUp(SignUpRequest signUpRequest);

	ResponseEntity<SignInResponse> signIn(SignInRequest signInRequest);

	ResponseEntity<?> reissue(ReissueRequest reissueRequest);

	ResponseEntity<Void> logout(LogoutRequest logoutRequest);

}
