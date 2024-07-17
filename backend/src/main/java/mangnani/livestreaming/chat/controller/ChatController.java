package mangnani.livestreaming.chat.controller;

import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.chat.dto.ChatMessageRequest;
import mangnani.livestreaming.chat.dto.ChatMessageResponse;
import mangnani.livestreaming.chat.repository.ChatMessageRepository;
import mangnani.livestreaming.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	@MessageMapping("/chat.sendMessage/{streamerId}/{broadcastId}")
	@SendTo("/topic/{streamerId}/{broadcastId}")
	public ChatMessageResponse sendMessage(@DestinationVariable String streamerId,
			@DestinationVariable String broadcastId, ChatMessageRequest chatMessageDto) {

		chatMessageDto.setStreamerId(streamerId);
		chatMessageDto.setBroadcastId(broadcastId);

		return chatService.saveChatMessage(chatMessageDto);
	}
}
