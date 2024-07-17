import ResponseDto from "../response.dto";

export default interface GetStarCandyResponseDto extends ResponseDto {
  nickname: string;
  starCandyAmount : number | null;
}