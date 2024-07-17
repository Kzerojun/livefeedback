package mangnani.livestreaming.payment.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {
	@Bean
	public IamportClient iamportClient(@Value("${spring.payment.api-key}") String apiKey,
			@Value("${spring.payment.secret-key}") String secretKey) {
		return new IamportClient(apiKey, secretKey);
	}

}
