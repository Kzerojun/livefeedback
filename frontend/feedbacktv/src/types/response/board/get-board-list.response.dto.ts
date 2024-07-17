import ResponseDto from "../response.dto";
import {BoardListItem} from "../../../types/interface";

export default interface GetBoardListResponseDto extends ResponseDto {
  boardList: BoardListItem[];
  totalBoardsSize: number;
}