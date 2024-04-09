package mangnani.livestreaming.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseDto {

	private String code;
	private String message;

	public static ResponseDto validationFailed() {
		return new ResponseDto(ResponseCode.VALIDATION_FAILED, ResponseMessage.VALIDATION_FAILED);
	}

}
