package mangnani.livestreaming.donation.service;

import mangnani.livestreaming.donation.dto.request.DonationRequest;
import mangnani.livestreaming.donation.dto.response.DonationResponse;
import mangnani.livestreaming.donation.dto.response.GetFanListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface DonationService {

	ResponseEntity<DonationResponse> donate(DonationRequest donationRequest);

	ResponseEntity<GetFanListResponse> getTop20FanMember(String userId, Pageable pageable);

	ResponseEntity<GetFanListResponse> getTop3FanMember(String userId, Pageable pageable);
}
