package mangnani.livestreaming.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {

	private String userLoginId;

	private String userNickname;

	private String content;

	private String streamerId;

	private String broadcastId;

}
