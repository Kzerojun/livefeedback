package mangnani.livestreaming.board.exception;

import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.BadRequestException;
import mangnani.livestreaming.global.exception.NotFoundException;

public class NoExistedBoard extends NotFoundException {

	public NoExistedBoard() {
		super(ResponseCode.NO_EXISTED_BOARD, ResponseMessage.NO_EXISTED_BOARD);
	}

}
