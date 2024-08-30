package mangnani.livestreaming.file.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class FileResponse extends ResponseDto {
	private final String url;

	private FileResponse(String url) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.url = url;
	}

	public static FileResponse success(String url) {
		return new FileResponse(url);
	}
}