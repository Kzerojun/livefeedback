package mangnani.livestreaming.donation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.donation.dto.request.DonationRequest;
import mangnani.livestreaming.donation.dto.response.DonationResponse;
import mangnani.livestreaming.donation.service.DonationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/donation")
@RequiredArgsConstructor
public class DonationController {

	private final DonationService donationService;

	@PostMapping
	public ResponseEntity<DonationResponse> donate(
			@Valid @RequestBody DonationRequest donationRequest) {
		return donationService.donate(donationRequest);
	}
}
