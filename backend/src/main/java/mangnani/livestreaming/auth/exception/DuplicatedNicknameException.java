package mangnani.livestreaming.auth.exception;


import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.BadRequestException;

public class DuplicatedNicknameException extends BadRequestException {

	public DuplicatedNicknameException() {
		super(ResponseMessage.DUPLICATED_NICKNAME);
	}
}
