import ResponseDto from "../response.dto";

export default interface GetBoardDetailResponseDto extends ResponseDto {
  title : string;
  content : string;
  images: string[];
}