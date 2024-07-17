package mangnani.livestreaming.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Stack;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String loginId;
	private String content;
	private String streamerId;
	private String broadcastId;

	@CreatedDate
	private LocalDateTime chat_at;

	@Builder
	public ChatMessage(String loginId, String content, String streamerId, String broadcastId) {
		this.loginId = loginId;
		this.content = content;
		this.streamerId = streamerId;
		this.broadcastId = broadcastId;
	}
}
