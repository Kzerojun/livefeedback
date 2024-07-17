package mangnani.livestreaming.broadcast.dto.response;


import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class StartBroadcastResponse extends ResponseDto {

	private final Long broadcastId;
	private StartBroadcastResponse(Long broadcastId) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.broadcastId = broadcastId;
	}

	public static StartBroadcastResponse success(Long broadcastId) {
		return new StartBroadcastResponse(broadcastId);
	}
}
