package mangnani.livestreaming.broadcast.repository;

import java.util.List;
import java.util.Optional;
import mangnani.livestreaming.broadcast.constant.BroadcastStatus;
import mangnani.livestreaming.broadcast.entity.Broadcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BroadcastRepository extends JpaRepository<Broadcast,Long> {

	List<Broadcast> findByCategoryAndStatus(String category, BroadcastStatus status);

	List<Broadcast> findByStatus(BroadcastStatus status);

	Optional<Broadcast> findBroadcastByStreamKey(String streamKey);

}
