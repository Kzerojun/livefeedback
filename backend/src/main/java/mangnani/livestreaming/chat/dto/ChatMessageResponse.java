package mangnani.livestreaming.chat.dto;

import lombok.Getter;

@Getter
public class ChatMessageResponse {

	private final String nickname;
	private final String content;

	private ChatMessageResponse(String nickname, String content) {
		this.nickname = nickname;
		this.content = content;
	}

	public static ChatMessageResponse create(String nickname, String content) {
		return new ChatMessageResponse(nickname, content);
	}
}
