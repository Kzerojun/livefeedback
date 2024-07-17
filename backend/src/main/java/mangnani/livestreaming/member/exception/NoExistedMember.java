package mangnani.livestreaming.member.exception;


import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.BadRequestException;
import mangnani.livestreaming.global.exception.NotFoundException;

public class NoExistedMember extends NotFoundException {

	public NoExistedMember() {
		super(ResponseCode.NO_EXISTED_MEMBER, ResponseMessage.NO_EXISTED_MEMBER);
	}
}
