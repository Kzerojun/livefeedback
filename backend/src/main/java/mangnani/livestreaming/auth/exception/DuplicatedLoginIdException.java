package mangnani.livestreaming.auth.exception;


import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.BadRequestException;

public class DuplicatedLoginIdException extends BadRequestException {


	public DuplicatedLoginIdException() {
		super(ResponseMessage.DUPLICATE_LOGIN_ID);
	}
}
