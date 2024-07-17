package mangnani.livestreaming.payment.repository;

import java.util.Optional;
import mangnani.livestreaming.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

	Optional<PaymentEntity> findPaymentEntityByPaymentUid(String paymentUid);

}
