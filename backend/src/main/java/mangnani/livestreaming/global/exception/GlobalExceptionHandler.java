package mangnani.livestreaming.global.exception;

import mangnani.livestreaming.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ResponseDto> handleBadRequestException(BadRequestException e) {
		return ResponseEntity.badRequest().body(new ResponseDto(e.getCode(), e.getMessage()));
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ResponseDto> handleUnauthorizedException(UnauthorizedException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ResponseDto(e.getCode(), e.getMessage()));
	}

	@ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
	public ResponseEntity<ResponseDto> validationExceptionHandler(Exception exception) {
		return ResponseEntity.badRequest().body(ResponseDto.validationFailed());
	}
}
