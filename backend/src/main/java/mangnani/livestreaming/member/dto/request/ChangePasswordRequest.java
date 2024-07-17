package mangnani.livestreaming.member.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordRequest {

	@NotBlank(message = "비밀번호를 입력해주세요")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "비밀번호(8~15 이내 대 소문,숫자,특수문자)")
	private String currentPassword;

	@NotBlank(message = "비밀번호를 입력해주세요")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "비밀번호(8~15 이내 대 소문,숫자,특수문자)")
	private String newPassword;

}
