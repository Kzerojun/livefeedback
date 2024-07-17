package mangnani.livestreaming.broadcast.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mangnani.livestreaming.broadcast.constant.BroadcastAttribute;
import mangnani.livestreaming.broadcast.constant.BroadcastCategory;

@Getter
@Setter
@NoArgsConstructor
public class StartBroadcastRequest {

	@NotBlank(message = "제목은 필수 입력 항목입니다.")
	private String title;

	@NotNull(message = "카테고리는 필수 입력 항목입니다.")
	private BroadcastCategory category;

	@NotNull(message = "방송 속성은 필수 입력 항목입니다.")
	private BroadcastAttribute attribute;

	@NotBlank(message = "스트리머 ID는 필수 입력 항목입니다.")
	private String streamerId;

	@NotBlank(message = "스트리머 닉네임은 필수 입력 항목입니다.")
	private String streamerNickname;

	@NotBlank(message = "스트림 키는 필수 입력 항목입니다.")
	private String streamKey;
}
