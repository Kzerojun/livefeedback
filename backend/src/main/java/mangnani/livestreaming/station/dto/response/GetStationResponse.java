package mangnani.livestreaming.station.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class GetStationResponse extends ResponseDto {

	private final String description;
	private final String image;

	private GetStationResponse(String description, String image) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.description = description;
		this.image = image;
	}

	public static GetStationResponse success(String description, String image) {
		return new GetStationResponse(description, image);
	}
}
