package mangnani.livestreaming.station.repository;

import java.util.Optional;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.station.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

	Optional<Station> findByMember(Member member);

	@Query(
			"SELECT s "
			+ "FROM Station s "
			+ "JOIN s.member m "
			+ "WHERE m.loginId= :loginId"
	)
	Optional<Station> findByMemberLoginId(String loginId);

}
