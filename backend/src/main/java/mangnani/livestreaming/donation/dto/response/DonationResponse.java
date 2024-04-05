package mangnani.livestreaming.donation.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class DonationResponse extends ResponseDto {

	private DonationResponse() {
		super(ResponseCode.DONATION_SUCCESS, ResponseMessage.DONATION_SUCCESS);
	}

	public static DonationResponse success() {
		return new DonationResponse();
	}
}
