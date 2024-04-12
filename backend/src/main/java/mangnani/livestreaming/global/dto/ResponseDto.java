package mangnani.livestreaming.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@Getter
public class ResponseDto {

	private String code;
	private String message;

	public static ResponseDto validationFailed() {
		return new ResponseDto(ResponseCode.VALIDATION_FAILED, ResponseMessage.VALIDATION_FAILED);
	}

	public static ResponseDto databaseError() {
		return new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
	}
}
