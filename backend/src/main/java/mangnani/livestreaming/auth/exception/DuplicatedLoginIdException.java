package mangnani.livestreaming.auth.exception;


import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.BadRequestException;

public class DuplicatedLoginIdException extends BadRequestException {

	public DuplicatedLoginIdException() {
		super(ResponseCode.DUPLICATED_LOGIN_ID,ResponseMessage.DUPLICATE_LOGIN_ID);
	}
}
