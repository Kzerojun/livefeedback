package mangnani.livestreaming.stream.repository;

import java.util.Optional;
import mangnani.livestreaming.stream.constant.StreamStatus;
import mangnani.livestreaming.stream.entity.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamRepository extends JpaRepository<Stream,Long> {

	Optional<Stream> findByStreamKeyAndStreamStatus(String streamKey, StreamStatus streamStatus);

}
