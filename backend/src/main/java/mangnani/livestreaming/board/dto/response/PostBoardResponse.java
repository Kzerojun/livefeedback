package mangnani.livestreaming.board.dto.response;


import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class PostBoardResponse extends ResponseDto {

	private PostBoardResponse() {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
	}

	public static PostBoardResponse success() {
		return new PostBoardResponse();
	}
}
