package mangnani.livestreaming.broadcast.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mangnani.livestreaming.broadcast.constant.BroadcastAttribute;
import mangnani.livestreaming.broadcast.constant.BroadcastCategory;
import mangnani.livestreaming.broadcast.constant.BroadcastStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Broadcast {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	@Enumerated(value = EnumType.STRING)
	private BroadcastCategory category;
	private String streamerId;
	private String streamerNickname;
	private String streamKey;


	@Enumerated(value = EnumType.STRING)
	private BroadcastAttribute attribute;

	@Enumerated(value = EnumType.STRING)
	private BroadcastStatus status;

	@CreatedDate
	private LocalDateTime start_at;

	@Builder
	public Broadcast(String title, BroadcastCategory category, String streamerId, String streamKey,
			BroadcastAttribute attribute,BroadcastStatus status,String streamerNickname) {
		this.title = title;
		this.category = category;
		this.attribute = attribute;
		this.streamerId = streamerId;
		this.streamerNickname = streamerNickname;
		this.streamKey = streamKey;
		this.status = status;
	}

	public void endBroadCast(BroadcastStatus status) {
		this.status = status;
	}
}
