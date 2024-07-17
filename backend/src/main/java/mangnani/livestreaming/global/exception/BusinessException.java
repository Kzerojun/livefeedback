package mangnani.livestreaming.global.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"stackTrace", "localizedMessage", "cause", "suppressed"})
public class BusinessException extends RuntimeException{

	public BusinessException(String message) {
		super(message);
	}
}
