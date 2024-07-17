package mangnani.livestreaming.file.exception;

import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.BadRequestException;

public class InvalidFileTypeException extends BadRequestException {

	public InvalidFileTypeException() {
		super(ResponseCode.INVALID_FILE_TYPE, ResponseMessage.INVALID_FILE_TYPE);
	}

}
