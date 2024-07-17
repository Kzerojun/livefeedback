package mangnani.livestreaming.global.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends BusinessException{

	private final String code;

	public NotFoundException(String code, String message) {
		super(message);
		this.code = code;
	}
}
