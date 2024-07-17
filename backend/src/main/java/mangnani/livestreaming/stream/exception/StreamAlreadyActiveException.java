package mangnani.livestreaming.stream.exception;

import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.BadRequestException;

public class StreamAlreadyActiveException extends BadRequestException {

	private StreamAlreadyActiveException() {
		super(ResponseCode.STREAM_ALREADY_ACTIVE, ResponseMessage.STREAM_ALREADY_ACTIVE);
	}

	public static StreamAlreadyActiveException alreadyActiveException() {
		return new StreamAlreadyActiveException();
	}

}
