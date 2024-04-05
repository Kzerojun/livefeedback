package mangnani.livestreaming.auth.exception;


import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.BadRequestException;

public class DuplicatedNicknameException extends BadRequestException {

	public DuplicatedNicknameException() {
		super(ResponseCode.DUPLICATED_NICKNAME,ResponseMessage.DUPLICATED_NICKNAME);
	}
}
