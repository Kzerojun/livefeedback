package mangnani.livestreaming.station.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class PatchStationImageResponse extends ResponseDto {

	private PatchStationImageResponse() {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
	}

	public static PatchStationImageResponse success() {
		return new PatchStationImageResponse();
	}

}
