package mangnani.livestreaming.boardcategory.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mangnani.livestreaming.board.entity.Board;
import mangnani.livestreaming.station.entity.Station;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String category;

	private String description;

	@ManyToOne
	@JoinColumn(name = "broadcast_station_id")
	private Station station;

	@OneToMany(mappedBy ="boardCategory")
	private List<Board> boards = new ArrayList<>();

	@Builder
	public BoardCategory(String category, String description,Station station) {
		this.category = category;
		this.description = description;
		this.station = station;
	}

}
