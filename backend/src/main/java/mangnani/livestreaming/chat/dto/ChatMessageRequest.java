package mangnani.livestreaming.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRequest {

	private String loginId;

	private String content;

	private String streamerId;

	private String broadcastId;

}
