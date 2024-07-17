package mangnani.livestreaming.payment.service.implement;

import com.siot.IamportRestClient.Iamport;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.siot.IamportRestClient.response.Prepare;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangnani.livestreaming.payment.constant.PaymentStatus;
import mangnani.livestreaming.payment.dto.request.PaymentRequest;
import mangnani.livestreaming.payment.dto.request.ValidatePaymentRequest;
import mangnani.livestreaming.payment.dto.response.PaymentResponse;
import mangnani.livestreaming.payment.entity.PaymentEntity;
import mangnani.livestreaming.payment.repository.PaymentRepository;
import mangnani.livestreaming.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	private final IamportClient api;
	private final PaymentRepository paymentRepository;

	@Override
	@Transactional
	public void postPrepare(PaymentRequest request) {
		PrepareData prepareData = new PrepareData(request.getPaymentUid(), request.getTotalPrice());
		try {
			IamportResponse<Prepare> iamportResponse = api.postPrepare(prepareData);
			paymentRepository.save(PaymentEntity.builder()
					.paymentUid(request.getPaymentUid())
					.productName(request.getProductName())
					.price(request.getTotalPrice())
					.status(PaymentStatus.Ready)
					.build());

			log.info("결과 코드 : {}", iamportResponse.getCode());
			log.info("결과 메시지 : {}", iamportResponse.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional
	@Override
	public ResponseEntity<PaymentResponse> validatePayment(ValidatePaymentRequest request) {
		try {
			log.info(request.getPrePaymentUid());
			PaymentEntity payment = paymentRepository.findPaymentEntityByPaymentUid(request.getPrePaymentUid())
					.orElseThrow();
			BigDecimal prePrice = payment.getPrice();

			log.info(request.getValidatePaymentUid());
			IamportResponse<Payment> iamportResponse = api.paymentByImpUid(request.getValidatePaymentUid());
			BigDecimal payAmount = iamportResponse.getResponse().getAmount();

			//환불
			if (!prePrice.equals(payAmount)) {
				log.error("먼가 이상함");
				payment.setPaymentFailedStatus();
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

			payment.setPaymentSuccessStatus();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().body(PaymentResponse.success());
	}
}
