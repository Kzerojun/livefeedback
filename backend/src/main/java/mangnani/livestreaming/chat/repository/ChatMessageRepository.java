package mangnani.livestreaming.chat.repository;

import java.util.List;
import mangnani.livestreaming.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	List<ChatMessage> findByStreamerIdAndBroadcastId(String streamerId, String broadcastId);
}
