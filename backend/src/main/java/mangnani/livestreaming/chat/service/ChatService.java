package mangnani.livestreaming.chat.service;

import mangnani.livestreaming.chat.dto.ChatMessageRequest;
import mangnani.livestreaming.chat.dto.ChatMessageResponse;

public interface ChatService {

	ChatMessageResponse saveChatMessage(ChatMessageRequest chatMessageDto);

}
