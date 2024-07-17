package mangnani.livestreaming.stream.entity;

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
import lombok.NoArgsConstructor;
import mangnani.livestreaming.stream.constant.StreamStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stream {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String streamKey;

	@Enumerated(value = EnumType.STRING)
	private StreamStatus streamStatus;

	private String ipAddress;

	@CreatedDate
	private LocalDateTime streamStartAt;

	@LastModifiedDate
	private LocalDateTime streamEndAt;

	@Builder
	public Stream(String streamKey, StreamStatus streamStatus, String ipAddress) {
		this.streamKey = streamKey;
		this.streamStatus = streamStatus;
		this.ipAddress = ipAddress;
	}

	public void updateLiveEnd(StreamStatus streamStatus) {
		this.streamStatus = streamStatus;
	}
}
