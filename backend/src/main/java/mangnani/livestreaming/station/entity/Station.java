package mangnani.livestreaming.station.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mangnani.livestreaming.boardcategory.entity.BoardCategory;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.boardcategory.constant.DefaultCategory;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Station {

	@Id
	@Column(name = "broadcast_station_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	private String image;

	@OneToOne(mappedBy = "station")
	private Member member;

	@OneToMany(mappedBy = "station",cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BoardCategory> boardCategories = new ArrayList<>();

	public static Station create() {
		Station station = new Station();
		station.getBoardCategories().addAll(
				Arrays.stream(DefaultCategory.values())
						.map(defaultCategory -> BoardCategory.builder()
								.category(defaultCategory.getName())
								.description(defaultCategory.getDescription())
								.station(station)
								.build()
						).toList()
		);
		return station;
	}

	public void updateImage(String image) {
		this.image = image;
	}

	public void updateDescription(String description) {
		this.description = description;
	}


	private void addBoardCategory(BoardCategory boardCategory) {
		this.boardCategories.add(boardCategory);
	}
}
