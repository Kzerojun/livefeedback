import ResponseDto from "../response.dto";

export  default interface GetIsFavoriteResponseDto extends ResponseDto {
  favorite : boolean;
}