package mangnani.livestreaming.global.exception;

import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;

public class NoPermissionTokenException extends UnauthorizedException {

	public NoPermissionTokenException() {
		super(ResponseCode.NO_PERMISSION_TOKEN, ResponseMessage.NO_PERMISSION_TOKEN);
	}
}
