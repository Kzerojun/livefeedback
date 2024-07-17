package mangnani.livestreaming.station.exception;

import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.BadRequestException;

public class NoExistedStation extends BadRequestException {

	public NoExistedStation() {
		super(ResponseCode.NO_EXISTED_STATION, ResponseMessage.NO_EXISTED_STATION);
	}
}
