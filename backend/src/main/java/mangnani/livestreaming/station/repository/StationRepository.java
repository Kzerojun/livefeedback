package mangnani.livestreaming.station.repository;

import java.util.Optional;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.station.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station,Long> {

	Optional<Station> findByMember(Member member);

}
