package mangnani.livestreaming.auth.service;

import mangnani.livestreaming.auth.dto.request.LogoutRequest;
import mangnani.livestreaming.auth.dto.request.ReissueRequest;
import mangnani.livestreaming.auth.dto.request.LoginRequest;
import mangnani.livestreaming.auth.dto.request.SignUpRequest;
import mangnani.livestreaming.auth.dto.response.LoginResponse;
import mangnani.livestreaming.auth.dto.response.SignUpResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

	ResponseEntity<SignUpResponse> signUp(SignUpRequest signUpRequest);

	ResponseEntity<LoginResponse> login(LoginRequest loginRequest);

	ResponseEntity<Void> logout(String userLoginId,String accessToken);

	ResponseEntity<?> reissue(ReissueRequest reissueRequest);


}
