package mangnani.livestreaming.boardcategory.dto.response;

import java.util.List;
import lombok.Getter;
import mangnani.livestreaming.boardcategory.dto.object.BoardCategoryItem;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class PostBoardCategoryResponse extends ResponseDto {

	List<BoardCategoryItem> categories;

	private PostBoardCategoryResponse() {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
	}

	public static PostBoardCategoryResponse success() {
		return new PostBoardCategoryResponse();
	}
}
