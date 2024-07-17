package mangnani.livestreaming.favorite.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String followerId;
	private String followeeId;

	@Builder
	private Favorite(String followeeId, String followerId) {
		this.followerId = followerId;
		this.followeeId = followeeId;
	}

	public static Favorite create(String followeeId, String followerId) {
		return Favorite.builder()
				.followeeId(followeeId)
				.followerId(followerId)
				.build();
	}
}
