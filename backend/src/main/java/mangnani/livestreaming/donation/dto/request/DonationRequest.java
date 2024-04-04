package mangnani.livestreaming.donation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DonationRequest {

	@NotBlank(message = "후원하는 멤버 아이디를 입력해주세요.")
	private String donorMemberLoginId;

	@NotBlank(message = "후원받는 멤버 아이디를 입력해주세요.")
	private String recipientMemberLoginId;

	@Min(value = 1, message = "1개이상 별사탕을 입력해주세요")
	private int starCandyAmount;

	private String message;
}
