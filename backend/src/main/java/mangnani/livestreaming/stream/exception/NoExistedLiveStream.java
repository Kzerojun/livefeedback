package mangnani.livestreaming.stream.exception;

import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.BadRequestException;

public class NoExistedLiveStream extends BadRequestException {

	public NoExistedLiveStream() {
		super(ResponseCode.NO_EXISTED_LIVE_STREAM, ResponseMessage.NO_EXISTED_LIVE_STREAM);
	}

}
