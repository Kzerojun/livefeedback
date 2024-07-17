package mangnani.livestreaming.payment.controller;

import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.payment.dto.request.PaymentRequest;
import mangnani.livestreaming.payment.dto.request.ValidatePaymentRequest;
import mangnani.livestreaming.payment.dto.response.PaymentResponse;
import mangnani.livestreaming.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/prepare")
	public void preparePayment(@RequestBody PaymentRequest request) {
		paymentService.postPrepare(request);
	}

	@PostMapping("/validate")
	public ResponseEntity<PaymentResponse> postValidate(@RequestBody ValidatePaymentRequest request) {
		return paymentService.validatePayment(request);
	}

}
