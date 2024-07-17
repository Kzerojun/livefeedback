package mangnani.livestreaming.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrePayment {

	@Id
	private String productUid;

	private BigDecimal amount;

	@Builder
	public PrePayment(String productUid, BigDecimal amount) {
		this.productUid = productUid;
		this.amount = amount;
	}
}
