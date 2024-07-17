package mangnani.livestreaming.station.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class PatchDescriptionResponse extends ResponseDto {

	private PatchDescriptionResponse() {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
	}

	public static PatchDescriptionResponse success() {
		return new PatchDescriptionResponse();
	}
}
