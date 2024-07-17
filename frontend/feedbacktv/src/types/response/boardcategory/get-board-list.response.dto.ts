import ResponseDto from "../response.dto";
import BoardCategory from "../../interface/board/board-category.interface";

export default interface GetBoardCategoryListResponseDto extends ResponseDto {
  boardCategoryItemList: BoardCategory[];
}