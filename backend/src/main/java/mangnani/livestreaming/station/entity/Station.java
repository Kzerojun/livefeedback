package mangnani.livestreaming.station.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import mangnani.livestreaming.member.entity.Member;

@Entity
@Getter
public class Station {

	@Id
	@Column(name = "broadcast_station_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	private String image;

	@OneToOne(mappedBy = "station")
	private Member member;

	public void updateImage(String image) {
		this.image = image;
	}

	public void updateDescription(String description) {
		this.description = description;
	}
}
