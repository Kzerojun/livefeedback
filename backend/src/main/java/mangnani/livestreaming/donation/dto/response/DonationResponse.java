package mangnani.livestreaming.donation.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseDto;

@Getter
public class DonationResponse extends ResponseDto {

	private static final String MESSAGE = "Donation Success";

	private DonationResponse() {
		super(MESSAGE);
	}

	public static DonationResponse success() {
		return new DonationResponse();
	}
}
