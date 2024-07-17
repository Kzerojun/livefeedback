package mangnani.livestreaming.member.repository;

import java.util.Optional;
import mangnani.livestreaming.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByLoginId(String loginId);

	boolean existsByNickname(String nickname);

	Optional<Member> findByLoginId(String loginId);

	@Query("SELECT m " +
			"FROM Member m " +
			"JOIN FETCH m.station s " +
			"LEFT JOIN FETCH s.boardCategories bc " +
			"WHERE m.loginId = :loginId")
	Optional<Member> findMemberByLoginIdWithStationAndBoardCategories(@Param("loginId") String loginId);

	@EntityGraph(attributePaths = {"station"})
	Optional<Member> findMemberWithStationByLoginId(String loginId);


}
