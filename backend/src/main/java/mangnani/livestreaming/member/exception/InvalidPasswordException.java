package mangnani.livestreaming.member.exception;

import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.BadRequestException;

public class InvalidPasswordException extends BadRequestException {

	public InvalidPasswordException() {
		super(ResponseCode.PASSWORD_MISMATCH, ResponseMessage.PASSWORD_MISMATCH);
	}
}
