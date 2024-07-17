package mangnani.livestreaming.favorite.repository;

import mangnani.livestreaming.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {


	boolean existsByFolloweeIdAndFollowerId(String followeeId, String followerId);
}
