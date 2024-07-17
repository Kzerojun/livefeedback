package mangnani.livestreaming.global.exception;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class NoPermissionTokenException extends UnauthorizedException {

	public NoPermissionTokenException() {
		super(ResponseCode.NO_PERMISSION_TOKEN, ResponseMessage.NO_PERMISSION_TOKEN);
	}
}
