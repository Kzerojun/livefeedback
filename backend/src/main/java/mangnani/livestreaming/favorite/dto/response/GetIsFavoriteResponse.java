package mangnani.livestreaming.favorite.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class GetIsFavoriteResponse extends ResponseDto {

	private final boolean isFavorite;

	private GetIsFavoriteResponse(boolean isFavorite) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.isFavorite = isFavorite;
	}

	public static GetIsFavoriteResponse success(boolean isFavorite) {
		return new GetIsFavoriteResponse(isFavorite);
	}
}
