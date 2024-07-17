package mangnani.livestreaming.board.repository;

import java.util.List;
import mangnani.livestreaming.board.entity.Board;
import mangnani.livestreaming.boardcategory.entity.BoardCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {

	List<Board> findBoardsByBoardCategory(BoardCategory boardCategory, Pageable pageable);

	int countByBoardCategory(BoardCategory boardCategory);

}
