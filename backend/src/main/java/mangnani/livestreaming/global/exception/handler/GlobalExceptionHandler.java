package mangnani.livestreaming.global.exception.handler;

import mangnani.livestreaming.auth.exception.LoginFailedException;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.exception.BadRequestException;
import mangnani.livestreaming.global.exception.NotFoundException;
import mangnani.livestreaming.global.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ResponseDto> handleBadRequestException(BadRequestException e) {
		return ResponseEntity.badRequest().body(new ResponseDto(e.getCode(), e.getMessage()));
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ResponseDto> handleNotFoundException(NotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getCode(),
				e.getMessage()));
	}
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ResponseDto> handleUnauthorizedException(UnauthorizedException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ResponseDto(e.getCode(), e.getMessage()));
	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<ResponseDto> validationExceptionHandler(MethodArgumentNotValidException e) {
		return ResponseEntity.badRequest().body(ResponseDto.validationFailed(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
	}

	@ExceptionHandler({BadCredentialsException.class})
	public ResponseEntity<LoginFailedException> handleBadCredentialException() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginFailedException());
	}

//	@ExceptionHandler(RuntimeException.class)
//	public ResponseEntity<ResponseDto> databaseExceptionHandler(Exception exception) {
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//				.body(ResponseDto.databaseError());
//	}

}
