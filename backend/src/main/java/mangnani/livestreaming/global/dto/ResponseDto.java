package mangnani.livestreaming.global.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class ResponseDto {

	private String code;
	private String message;

	public static ResponseDto validationFailed(String message) {
		return new ResponseDto(ResponseCode.VALIDATION_FAILED, message);
	}

	public static ResponseDto databaseError() {
		return new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
	}
}
