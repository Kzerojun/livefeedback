package mangnani.livestreaming.donation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.donation.dto.request.DonationRequest;
import mangnani.livestreaming.donation.dto.response.DonationResponse;
import mangnani.livestreaming.donation.dto.response.GetFanListResponse;
import mangnani.livestreaming.donation.service.DonationService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/{userId}/top20fanList")
	public ResponseEntity<GetFanListResponse> getTop20FanList(@PathVariable String userId,
			@PageableDefault(size = 20, direction = Direction.DESC) Pageable pageable) {
		return donationService.getTop20FanMember(userId, pageable);
	}

	@GetMapping("/{userId}/top3fanList")
	public ResponseEntity<GetFanListResponse> getTop3FanList(@PathVariable(name = "userId") String userId,
			@PageableDefault(size = 3, direction = Direction.DESC) Pageable pageable
			) {
		return donationService.getTop3FanMember(userId, pageable);
	}
}
