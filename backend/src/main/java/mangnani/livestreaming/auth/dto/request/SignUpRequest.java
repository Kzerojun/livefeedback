package mangnani.livestreaming.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

	@NotBlank(message = "아이디를 입력해주세요")
	@Pattern(regexp = "^[0-9A-Za-z]{6,15}$", message = "숫자와 영문만 가능합니다. 길이는 6자 이상 15자이하입니다.")
	private String loginId;

	@NotBlank(message = "비밀번호를 입력해주세요")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "비밀번호(8~15 이내 대 소문,숫자,특수문자)")
	private String password;

	@NotBlank(message = "닉네임을 입력해주세요.")
	@Pattern(regexp = "^[가-힣A-Za-z0-9!@#$%^&*()_+]{2,7}$", message = "닉네임(2~7 국문,영어,숫자,특수문자)")
	private String nickname;

}
