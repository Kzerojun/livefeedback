package mangnani.livestreaming.boardcategory.repository;

import java.util.List;
import java.util.Optional;
import mangnani.livestreaming.boardcategory.entity.BoardCategory;
import mangnani.livestreaming.station.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory,Long> {

	Optional<BoardCategory> findByCategoryAndStation(String name, Station station);

	List<BoardCategory> findByStation(Station station);

}
