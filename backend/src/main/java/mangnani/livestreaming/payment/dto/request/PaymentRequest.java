package mangnani.livestreaming.payment.dto.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequest {

	private String paymentUid;

	private String productName;

	private BigDecimal totalPrice;
}
