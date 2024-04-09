package mangnani.livestreaming.donation.service.implement;

import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.donation.dto.request.DonationRequest;
import mangnani.livestreaming.donation.dto.response.DonationResponse;
import mangnani.livestreaming.donation.entity.Donation;
import mangnani.livestreaming.donation.repository.DonationRepository;
import mangnani.livestreaming.donation.service.DonationService;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import mangnani.livestreaming.member.service.MemberService;
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
				.startCandyAmount(donationRequest.getStarCandyAmount())
				.message(donationRequest.getMessage())
				.build();

		donationRepository.save(donation);

		return ResponseEntity.ok().body(DonationResponse.success());
	}
}
