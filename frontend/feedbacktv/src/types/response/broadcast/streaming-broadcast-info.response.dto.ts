import ResponseDto from "../response.dto";

export default interface StreamingBroadcastInfoResponseDto extends ResponseDto {
  title : string;

  streamerId : string;
  streamerNickname : string;
  streamKey : string;
}