package mangnani.livestreaming.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReissueRequest {

	@NotBlank(message = "accessToken 입력해주세요.")
	private String accessToken;

	@NotBlank(message = "refreshToken 입력해주세요.")
	private String refreshToken;

}
