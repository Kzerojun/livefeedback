package mangnani.livestreaming.boardcategory.exception;

import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.global.exception.BadRequestException;

public class NoExistedBoardCategory extends BadRequestException {

	public NoExistedBoardCategory() {
		super(ResponseCode.NO_EXISTED_BOARD_CATEGORY, ResponseMessage.NO_EXISTED_BOARD_CATEGORY);
	}

}
