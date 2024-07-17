package mangnani.livestreaming.payment.service;

import mangnani.livestreaming.payment.dto.request.PaymentRequest;
import mangnani.livestreaming.payment.dto.request.ValidatePaymentRequest;
import mangnani.livestreaming.payment.dto.response.PaymentResponse;
import org.springframework.http.ResponseEntity;

public interface PaymentService {

	void postPrepare(PaymentRequest request);

	ResponseEntity<PaymentResponse> validatePayment(ValidatePaymentRequest request);
}
