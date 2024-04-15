package mangnani.livestreaming.donation.service.implement;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.donation.dto.object.MemberItem;
import mangnani.livestreaming.donation.dto.request.DonationRequest;
import mangnani.livestreaming.donation.dto.response.DonationResponse;
import mangnani.livestreaming.donation.dto.response.GetFanListResponse;
import mangnani.livestreaming.donation.entity.Donation;
import mangnani.livestreaming.donation.repository.DonationRepository;
import mangnani.livestreaming.donation.repository.projection.MemberView;
import mangnani.livestreaming.donation.service.DonationService;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonationServiceImpl implements DonationService {

	private final DonationRepository donationRepository;

	private final MemberRepository memberRepository;

	@Override
	public ResponseEntity<DonationResponse> donate(DonationRequest donationRequest) {
		Member donorMember = memberRepository.findByLoginId(donationRequest.getDonorMemberLoginId())
				.orElseThrow(NoExistedMember::new);

		Member recipientMember = memberRepository.findByLoginId(donationRequest.getRecipientMemberLoginId())
				.orElseThrow(NoExistedMember::new);

		Donation donation = Donation.builder()
				.donor(donorMember)
				.recipient(recipientMember)
				.starCandyAmount(donationRequest.getStarCandyAmount())
				.message(donationRequest.getMessage())
				.build();

		donationRepository.save(donation);

		return ResponseEntity.ok().body(DonationResponse.success());
	}

	@Override
	public ResponseEntity<GetFanListResponse> getTop20FanMember(String userId, Pageable pageable) {
		List<MemberView> memberViews = donationRepository.findTop20DonorsByStarCandyAmountForRecipient(
				userId, pageable);

		return ResponseEntity.ok().body(GetFanListResponse.success(memberViews));
	}

	@Override
	public ResponseEntity<GetFanListResponse> getTop3FanMember(String userId, Pageable pageable) {
		List<MemberView> memberViews = donationRepository.findTop3DonorsByStarCandyAmountForRecipient(
						userId, pageable);

		return ResponseEntity.ok().body(GetFanListResponse.success(memberViews));
	}
}
