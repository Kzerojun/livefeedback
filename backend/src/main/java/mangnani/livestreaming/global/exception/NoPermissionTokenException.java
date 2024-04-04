package mangnani.livestreaming.global.exception;

public class NoPermissionTokenException extends UnauthorizedException {

	private static final String MESSAGE = "권한이 없는 토큰입니다.";

	public NoPermissionTokenException() {
		super(MESSAGE);
	}
}
