package mangnani.livestreaming.favorite.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostFavoriteRequest {

	@NotBlank
	private String followeeId;

	@NotBlank
	private String followerId;
}
