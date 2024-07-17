package mangnani.livestreaming.image.repository;

import java.util.List;
import mangnani.livestreaming.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {

	List<Image> findByBoardNumber(Long boardNumber);

}
