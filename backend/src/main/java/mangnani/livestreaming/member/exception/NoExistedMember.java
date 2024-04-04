package mangnani.livestreaming.member.exception;


import mangnani.livestreaming.global.exception.BadRequestException;

public class NoExistedMember extends BadRequestException {

	private static final String MESSAGE = "멤버가 존재하지 않습니다.";

	public NoExistedMember() {
		super(MESSAGE);
	}
}
