package mangnani.livestreaming.broadcast.dto.response;

import java.util.List;
import lombok.Getter;
import mangnani.livestreaming.broadcast.dto.object.BroadcastInfo;
import mangnani.livestreaming.broadcast.entity.Broadcast;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class GetBroadcastListResponse extends ResponseDto {

	private final List<BroadcastInfo> broadcastInfos;

	private GetBroadcastListResponse(List<BroadcastInfo> broadcastInfos) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.broadcastInfos = broadcastInfos;
	}

	public static GetBroadcastListResponse success(List<BroadcastInfo> broadcastInfos) {
		return new GetBroadcastListResponse(broadcastInfos);
	}
}
