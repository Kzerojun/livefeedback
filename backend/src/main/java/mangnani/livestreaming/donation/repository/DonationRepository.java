package mangnani.livestreaming.donation.repository;

import java.util.List;
import mangnani.livestreaming.donation.dto.object.MemberItem;
import mangnani.livestreaming.donation.entity.Donation;

import mangnani.livestreaming.donation.repository.projection.MemberView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DonationRepository extends JpaRepository<Donation, Long> {

	@Query("SELECT d.donor.loginId as loginId, d.donor.nickname as nickname, d.donor.profileImage as profileImage "
			+ "FROM Donation d "
			+ "WHERE d.recipient.loginId = :recipientId "
			+ "GROUP BY d.donor "
			+ "ORDER BY SUM(d.starCandyAmount) DESC "
	)
	List<MemberView> findTop20DonorsByStarCandyAmountForRecipient(String recipientId,
			Pageable pageable);

	@Query("SELECT d.donor.loginId as loginId, d.donor.nickname as nickname, d.donor.profileImage as profileImage "
			+ "FROM Donation d "
			+ "WHERE d.recipient.loginId = :recipientId "
			+ "GROUP BY d.donor "
			+ "ORDER BY SUM(d.starCandyAmount) DESC "
	)
	List<MemberView> findTop3DonorsByStarCandyAmountForRecipient(String recipientId,
			Pageable pageable);
}
