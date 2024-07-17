package mangnani.livestreaming.image.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sequence;

	private Long boardNumber;
	private String image;

	@Builder
	public Image(Long boardNumber, String image) {
		this.boardNumber = boardNumber;
		this.image = image;
	}
}
