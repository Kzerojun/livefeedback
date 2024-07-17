package mangnani.livestreaming.board.dto.response;

import java.util.List;
import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.image.entity.Image;

@Getter
public class GetBoardDetailResponse extends ResponseDto {

	private final String title;

	private final String content;

	private final List<String> images;

	private GetBoardDetailResponse(String title, String content, List<String> images) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.title = title;
		this.content = content;
		this.images = images;
	}

	public static GetBoardDetailResponse success(String title, String content, List<String> images) {
		return new GetBoardDetailResponse(title,content,images);
	}
}
