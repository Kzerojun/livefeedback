import ResponseDto from "../response.dto";
import BroadcastInfoResponseDto from "./broadcast-info-response.dto";

export default interface BroadcastsInfoResponseDto extends ResponseDto {
  broadcastInfos: BroadcastInfoResponseDto[];
}