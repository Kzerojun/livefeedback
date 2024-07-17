package mangnani.livestreaming.favorite.dto.response;

import lombok.Getter;
import mangnani.livestreaming.board.dto.response.PostBoardResponse;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class PostFavoriteResponse extends ResponseDto {

	private PostFavoriteResponse() {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
	}

	public static PostFavoriteResponse success() {
		return new PostFavoriteResponse();
	}
}
