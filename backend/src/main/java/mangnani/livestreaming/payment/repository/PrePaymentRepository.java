package mangnani.livestreaming.payment.repository;

import mangnani.livestreaming.payment.entity.PrePayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrePaymentRepository extends JpaRepository<PrePayment,String> {

}
