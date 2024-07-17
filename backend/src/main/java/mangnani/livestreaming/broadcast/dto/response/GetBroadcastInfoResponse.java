package mangnani.livestreaming.broadcast.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class GetBroadcastInfoResponse extends ResponseDto {

	private final String streamerNickname;
	private final String title;
	private final String streamKey;

	private GetBroadcastInfoResponse(String streamerNickname, String title, String streamKey) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.streamerNickname = streamerNickname;
		this.title = title;
		this.streamKey = streamKey;
	}

	public static GetBroadcastInfoResponse success(String streamerNickname, String	 title,
			String streamKey) {
		return new GetBroadcastInfoResponse(streamerNickname, title, streamKey);
	}
}
