package mangnani.livestreaming.donation.service.implement;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import mangnani.livestreaming.donation.dto.object.MemberItem;
import mangnani.livestreaming.donation.dto.request.DonationRequest;
import mangnani.livestreaming.donation.dto.response.DonationResponse;
import mangnani.livestreaming.donation.dto.response.GetFanListResponse;
import mangnani.livestreaming.donation.entity.Donation;
import mangnani.livestreaming.donation.repository.DonationRepository;
import mangnani.livestreaming.donation.repository.projection.MemberView;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DonationServiceImplTest {

	@Mock
	private DonationRepository donationRepository;

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
		when(memberRepository.findByLoginId(donationRequest.getDonorMemberLoginId())).thenReturn(
				Optional.of(donorMember));
		when(memberRepository.findByLoginId(donationRequest.getRecipientMemberLoginId())).thenReturn(Optional.of(recipientMember));

		ResponseEntity<DonationResponse> response = donationService.donate(donationRequest);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getMessage()).isEqualTo("도네이션 성공");
	}


	@DisplayName("도네이션 실패 후원자 유저 존재 X")
	@Test
	void donate_Fail_NoExistedDonorUser() {
		DonationRequest donationRequest = donationRequest();
		when(memberRepository.findByLoginId(donationRequest.getDonorMemberLoginId())).thenThrow(
				new NoExistedMember());

		assertThatThrownBy(() -> donationService.donate(donationRequest)).isInstanceOf(
				NoExistedMember.class);

		NoExistedMember noExistedMember = assertThrows(NoExistedMember.class,
				() -> donationService.donate(donationRequest));

		assertThat(noExistedMember.getCode()).isEqualTo("NEM");
		assertThat(noExistedMember.getMessage()).isEqualTo("멤버가 존재하지 않습니다.");
	}

	@DisplayName("도네이션 실패 후원받는 유저 존재 X")
	@Test
	void donate_Fail_NoExistedRecipientUser() {
		DonationRequest donationRequest = donationRequest();
		Member mockedMember = mock(Member.class);
		when(memberRepository.findByLoginId(donationRequest.getDonorMemberLoginId())).thenReturn(Optional.of(mockedMember));
		when(memberRepository.findByLoginId(donationRequest.getRecipientMemberLoginId())).thenThrow(new NoExistedMember());


		assertThatThrownBy(() -> donationService.donate(donationRequest)).isInstanceOf(
				NoExistedMember.class);

		NoExistedMember noExistedMember = assertThrows(NoExistedMember.class,
				() -> donationService.donate(donationRequest));

		assertThat(noExistedMember.getCode()).isEqualTo("NEM");
		assertThat(noExistedMember.getMessage()).isEqualTo("멤버가 존재하지 않습니다.");
	}

	@DisplayName("후원 Top20명 가져오기")
	@Test
	void getTop20FanMember() {
		List<MemberView> mockData = IntStream.range(0, 20)
				.mapToObj(i -> new MockMemberView("tester" + i, "tester" + i, "profile"))
				.collect(Collectors.toList());

		when(donationRepository.findTop20DonorsByStarCandyAmountForRecipient(eq("tester4"), any(Pageable.class)))
				.thenReturn(mockData);

		ResponseEntity<GetFanListResponse> response = donationService.getTop20FanMember("tester4", Pageable.unpaged());

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getMessage()).isEqualTo("성공");
		assertThat(response.getBody().getCode()).isEqualTo("SU");

		List<MemberItem> fans = response.getBody().getFans();
		assertThat(fans).isNotNull();
		assertThat(fans.size()).isEqualTo(20);
	}

	@DisplayName("후원 Top3명 가져오기")
	@Test
	void getTop3FanMember() {
		List<MemberView> mockMemberViews = Arrays.asList(
				new MockMemberView("tester1", "tester1", "profile"),
				new MockMemberView("tester2", "tester2", "profile"),
				new MockMemberView("tester3", "tester3", "profile")
		);


		when(donationRepository.findTop3DonorsByStarCandyAmountForRecipient(eq("tester4"), any(Pageable.class)))
				.thenReturn(mockMemberViews);

		ResponseEntity<GetFanListResponse> response = donationService.getTop3FanMember("tester4", Pageable.ofSize(3));

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getMessage()).isEqualTo("성공");
		assertThat(response.getBody().getCode()).isEqualTo("SU");
		assertThat(response.getBody().getFans().size()).isEqualTo(3);
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

	private class MockMemberView implements MemberView {

		private String loginId;
		private String nickname;
		private String profileImage;

		MockMemberView(String loginId, String nickname, String profileImage) {
			this.loginId = loginId;
			this.nickname = nickname;
			this.profileImage = profileImage;
		}

		@Override
		public String getLoginId() {
			return null;
		}

		@Override
		public String getProfileImage() {
			return null;
		}

		@Override
		public String getNickname() {
			return null;
		}
	}


}