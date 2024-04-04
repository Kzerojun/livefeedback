package mangnani.livestreaming.donation.service;

import mangnani.livestreaming.donation.dto.request.DonationRequest;
import mangnani.livestreaming.donation.dto.response.DonationResponse;
import org.springframework.http.ResponseEntity;

public interface DonationService {

	ResponseEntity<DonationResponse> donate(DonationRequest donationRequest);
}
