package mangnani.livestreaming.chat.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangnani.livestreaming.chat.dto.ChatMessageRequest;
import mangnani.livestreaming.chat.dto.ChatMessageResponse;
import mangnani.livestreaming.chat.entity.ChatMessage;
import mangnani.livestreaming.chat.repository.ChatMessageRepository;
import mangnani.livestreaming.chat.service.ChatService;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

	private final ChatMessageRepository chatMessageRepository;
	private final MemberRepository memberRepository;

	@Override
	public ChatMessageResponse saveChatMessage(ChatMessageRequest request) {

		Member member = memberRepository.findByLoginId(request.getLoginId())
				.orElseThrow(NoExistedMember::new);

		ChatMessage chatMessage = ChatMessage.builder()
				.broadcastId(request.getBroadcastId())
				.loginId(request.getLoginId())
				.content(request.getContent())
				.streamerId(request.getStreamerId())
				.build();

		chatMessageRepository.save(chatMessage);

		return ChatMessageResponse.create(member.getNickname(), chatMessage.getContent());
	}
}
