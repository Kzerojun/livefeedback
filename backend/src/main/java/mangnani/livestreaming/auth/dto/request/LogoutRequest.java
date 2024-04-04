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
public class LogoutRequest {

	@NotBlank(message = "잘못된 요청입니다")
	private String accessToken;

	@NotBlank(message = "잘못된 요청입니다")
	private String refreshToken;

}
