package mangnani.livestreaming.global.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends BusinessException {

	private final String code;

	public BadRequestException(String code, String message) {
		super(message);
		this.code = code;
	}
}