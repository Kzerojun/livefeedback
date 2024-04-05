package mangnani.livestreaming.global.exception;

public class ExceptionResponse {

	private final String message;

	private ExceptionResponse(String message) {
		this.message = message;
	}

	public static ExceptionResponse from(String message) {
		return new ExceptionResponse(message);
	}
}
