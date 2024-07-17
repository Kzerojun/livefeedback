package mangnani.livestreaming.payment.dto.response;

import lombok.Getter;
import mangnani.livestreaming.global.dto.ResponseCode;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;

@Getter
public class PaymentResponse extends ResponseDto {

	private PaymentResponse () {
		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
	}

	public static PaymentResponse success() {
		return new PaymentResponse();
	}

}
