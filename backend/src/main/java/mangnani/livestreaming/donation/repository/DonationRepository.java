package mangnani.livestreaming.donation.repository;

import mangnani.livestreaming.donation.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation,Long> {

}
