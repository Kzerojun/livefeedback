package mangnani.livestreaming.auth.exception;


import mangnani.livestreaming.global.exception.BadRequestException;

public class DuplicatedEmailException extends BadRequestException {

	private static final String MESSAGE = "중복된 이메일 입니다.";

	public DuplicatedEmailException() {
		super(MESSAGE);
	}
}
