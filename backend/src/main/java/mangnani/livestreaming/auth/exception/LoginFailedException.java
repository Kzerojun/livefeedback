package mangnani.livestreaming.auth.exception;


import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.UnauthorizedException;

public class LoginFailedException extends UnauthorizedException {

	public LoginFailedException() {
		super(ResponseMessage.LOGIN_FAILED);
	}
}
