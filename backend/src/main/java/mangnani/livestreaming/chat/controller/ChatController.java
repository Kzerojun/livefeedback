package mangnani.livestreaming.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangnani.livestreaming.chat.dto.ChatMessageRequest;
import mangnani.livestreaming.chat.dto.ChatMessageResponse;
import mangnani.livestreaming.chat.repository.ChatMessageRepository;
import mangnani.livestreaming.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ChatController {

	private final ChatService chatService;

	@MessageMapping("/chat/{streamerId}/{broadcastId}")
	@SendTo("/topic/{streamerId}/{broadcastId}")
	public ChatMessageResponse sendMessage(@Payload ChatMessageRequest chatMessageDto) {
		return chatService.saveChatMessage(chatMessageDto);
	}
}
