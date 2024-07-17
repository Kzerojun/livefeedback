import ResponseDto from "../response.dto";

export default  interface BroadcastInfoResponseDto extends ResponseDto{
  streamerNickname : string;
  broadcastId : number;

  title : string;
  streamerId : string;
  profileImage : string | null;
  thumbnail : string;
}