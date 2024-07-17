package mangnani.livestreaming.boardcategory.dto.response;


import java.util.List;
import lombok.Getter;
import mangnani.livestreaming.boardcategory.dto.object.BoardCategoryItem;
import mangnani.livestreaming.boardcategory.entity.BoardCategory;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class GetBoardCategoriesResponse extends ResponseDto {

	private final List<BoardCategoryItem> boardCategoryItemList;

	private GetBoardCategoriesResponse(List<BoardCategory> boardCategories) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		boardCategoryItemList = BoardCategoryItem.getList(boardCategories);
	}

	public static GetBoardCategoriesResponse success(List<BoardCategory> boardCategories) {
		return new GetBoardCategoriesResponse(boardCategories);
	}
}
