package mangnani.livestreaming.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeNicknameRequest {

	private String newNickname;
}
