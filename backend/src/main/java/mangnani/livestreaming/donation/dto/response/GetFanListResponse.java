package mangnani.livestreaming.donation.dto.response;

import java.util.List;
import lombok.Getter;
import mangnani.livestreaming.donation.dto.object.MemberItem;
import mangnani.livestreaming.donation.repository.projection.MemberView;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class GetFanListResponse extends ResponseDto {

	private final List<MemberItem> fans;

	private GetFanListResponse(List<MemberView> memberViews) {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
		this.fans = MemberItem.getList(memberViews);
	}

	public static GetFanListResponse success(List<MemberView> memberViews) {
		return new GetFanListResponse(memberViews);
	}
}
