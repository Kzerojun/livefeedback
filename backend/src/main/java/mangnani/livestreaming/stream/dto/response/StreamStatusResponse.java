package mangnani.livestreaming.stream.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.stream.constant.StreamStatus;

@Getter
public class StreamStatusResponse extends ResponseDto {

	private final StreamStatus streamStatus;

	private StreamStatusResponse(StreamStatus streamStatus) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.streamStatus = streamStatus;
	}

	public static StreamStatusResponse success(StreamStatus streamStatus) {
		return new StreamStatusResponse(streamStatus);
	}
}
