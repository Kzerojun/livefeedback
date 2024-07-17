package mangnani.livestreaming.broadcast.exception;

import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.BadRequestException;
import mangnani.livestreaming.global.exception.NotFoundException;

public class NoExistedBroadcast extends NotFoundException {

	public NoExistedBroadcast() {
		super(ResponseCode.NO_EXISTED_BROADCAST, ResponseMessage.NO_EXISTED_BROADCAST);
	}

}
