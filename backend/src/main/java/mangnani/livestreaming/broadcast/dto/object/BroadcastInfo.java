package mangnani.livestreaming.broadcast.dto.object;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mangnani.livestreaming.broadcast.entity.Broadcast;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BroadcastInfo {

	private String title;
	private String streamerId;
	private String profileImage;
	private Long broadcastId;
	private String thumbnail;

	@Builder
	public BroadcastInfo(String title, String streamerId, String profileImage,Long broadcastId,String thumbnail) {
		this.title = title;
		this.streamerId = streamerId;
		this.profileImage = profileImage;
		this.broadcastId = broadcastId;
		this.thumbnail = thumbnail;
	}

	public static List<BroadcastInfo> getList(List<Broadcast> broadcasts,String profileImage,String thumbnail) {
		return broadcasts.stream()
				.map(broadcast ->
						BroadcastInfo.builder()
								.title(broadcast.getTitle())
								.streamerId(broadcast.getStreamerId())
								.thumbnail(thumbnail)
								.profileImage(profileImage)
								.broadcastId(broadcast.getId())
								.build()
				).toList();
	}

}
