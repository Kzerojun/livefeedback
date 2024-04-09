package mangnani.livestreaming.global.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends BusinessException {
	private final String code;

	public UnauthorizedException(String code, String message) {
		super(message);
		this.code = code;
	}
}
