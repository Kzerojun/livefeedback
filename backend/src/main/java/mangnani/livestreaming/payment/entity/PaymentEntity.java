package mangnani.livestreaming.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mangnani.livestreaming.payment.constant.PaymentStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.parameters.P;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String productName;

	private BigDecimal price;

	private String paymentUid;

	@Enumerated(EnumType.STRING)
	private PaymentStatus status;

	@LastModifiedDate
	private LocalDateTime purchased_at;

	@Builder
	public PaymentEntity(String productName, BigDecimal price, String paymentUid, PaymentStatus status) {
		this.productName = productName;
		this.price = price;
		this.paymentUid = paymentUid;
		this.status = status;
	}

	public void setPaymentSuccessStatus() {
		this.status = PaymentStatus.SUCCESS;
	}

	public void setPaymentFailedStatus() {
		this.status = PaymentStatus.CANCEL;
	}
}
