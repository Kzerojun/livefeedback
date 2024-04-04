package mangnani.livestreaming.auth.exception;


import mangnani.livestreaming.global.exception.BadRequestException;

public class DuplicatedNicknameException extends BadRequestException {

	private static final String MESSAGE = "중복된 닉네임 입니다.";

	public DuplicatedNicknameException() {
		super(MESSAGE);
	}
}
