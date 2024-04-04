package mangnani.livestreaming.donation.service.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import mangnani.livestreaming.donation.dto.request.DonationRequest;
import mangnani.livestreaming.donation.dto.response.DonationResponse;
import mangnani.livestreaming.donation.entity.Donation;
import mangnani.livestreaming.donation.repository.DonationRepository;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import mangnani.livestreaming.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DonationServiceImplTest {

	@Mock
	private DonationRepository donationRepository;

	@Mock
	private MemberService memberService;

	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private DonationServiceImpl donationService;


	@DisplayName("도네이션 성공")
	@Test
	void donate_Success() {
		DonationRequest donationRequest = donationRequest();
		Member donorMember = donorMember();
		Member recipientMember = recipientMember();
		when(memberService.findByLoginId(donationRequest.getDonorMemberLoginId())).thenReturn(donorMember);
		when(memberService.findByLoginId(donationRequest.getRecipientMemberLoginId())).thenReturn(recipientMember);

		Donation donation = donation(donorMember, recipientMember, donationRequest);

		ResponseEntity<DonationResponse> response = donationService.donate(donationRequest);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getMessage()).isEqualTo("Donation Success");
	}


	@DisplayName("도네이션 실패 후원자 유저 존재 X")
	@Test
	void donate_Fail_NoExistedDonorUser() {
		DonationRequest donationRequest = donationRequest();
		when(memberService.findByLoginId(donationRequest.getDonorMemberLoginId())).thenThrow(
				NoExistedMember.class);

		assertThatThrownBy(() -> donationService.donate(donationRequest)).isInstanceOf(
				NoExistedMember.class);
	}

	@DisplayName("도네이션 실패 후원받는 유저 존재 X")
	@Test
	void donate_Fail_NoExistedRecipientUser() {
		DonationRequest donationRequest = donationRequest();
		when(memberService.findByLoginId(donationRequest.getRecipientMemberLoginId())).thenThrow(
				NoExistedMember.class);

		when(memberService.findByLoginId(donationRequest.getDonorMemberLoginId())).thenReturn(any());
		assertThatThrownBy(() -> donationService.donate(donationRequest)).isInstanceOf(
				NoExistedMember.class);
	}


	private DonationRequest donationRequest() {
		DonationRequest donationRequest = new DonationRequest();
		donationRequest.setDonorMemberLoginId("test1@gmail.com");
		donationRequest.setRecipientMemberLoginId("test2@gmail.com");
		donationRequest.setMessage("donation");
		donationRequest.setStarCandyAmount(1);
		return donationRequest;
	}

	private Member donorMember() {
		return Member.builder()
				.loginId("test1@gmail.com")
				.nickname("nickname")
				.password("1234")
				.build();

	}

	private Member recipientMember() {
		return Member.builder()
				.loginId("test2@gmail.com")
				.nickname("nickname")
				.password("1234")
				.build();
	}

	private Donation donation(Member donorMember, Member recipientMember, DonationRequest donationRequest) {
		return Donation.builder()
				.donor(donorMember)
				.recipient(recipientMember)
				.startCandyAmount(donationRequest.getStarCandyAmount())
				.message(donationRequest.getMessage())
				.build();
	}

}