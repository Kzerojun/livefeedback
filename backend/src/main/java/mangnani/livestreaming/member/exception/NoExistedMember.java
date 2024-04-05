package mangnani.livestreaming.member.exception;


import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.BadRequestException;

public class NoExistedMember extends BadRequestException {

	public NoExistedMember() {
		super(ResponseCode.NO_EXISTED_MEMBER, ResponseMessage.NO_EXISTED_MEMBER);
	}
}
