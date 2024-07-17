package mangnani.livestreaming.board.dto.response;

import java.util.List;
import lombok.Getter;
import mangnani.livestreaming.board.dto.object.BoardItem;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class GetBoardListResponse extends ResponseDto {

	private final List<BoardItem> boardItems;
	private final int totalBoardsSize;
	private GetBoardListResponse(List<BoardItem> boardItems,int totalBoardsSize) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.boardItems = boardItems;
		this.totalBoardsSize = totalBoardsSize;
	}

	public static GetBoardListResponse success(List<BoardItem> boardItems,int totalBoardsSize) {
		return new GetBoardListResponse(boardItems, totalBoardsSize);
	}
}
