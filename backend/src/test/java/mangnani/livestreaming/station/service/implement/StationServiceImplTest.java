package mangnani.livestreaming.station.service.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.repository.MemberRepository;
import mangnani.livestreaming.station.dto.request.PatchDescriptionRequest;
import mangnani.livestreaming.station.dto.request.PatchStationImageRequest;
import mangnani.livestreaming.station.dto.response.PatchDescriptionResponse;
import mangnani.livestreaming.station.dto.response.PatchStationImageResponse;
import mangnani.livestreaming.station.entity.Station;
import mangnani.livestreaming.station.exception.NoExistedStation;
import mangnani.livestreaming.station.repository.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class StationServiceImplTest {

	@Mock
	private StationRepository stationRepository;

	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private StationServiceImpl stationService;

	@DisplayName("방송국 이미지 변경 성공")
	@Test
	void update_Image_SUCCESS() {
		PatchStationImageRequest patchStationImageRequest = patchStationImageRequest();
		Station station = mock(Station.class);
		Member member = mock(Member.class);
		String userLoginId = "test";
		when(memberRepository.findByLoginId(any())).thenReturn(Optional.of(member));
		when(stationRepository.findByMember(member)).thenReturn(Optional.of(station));

		ResponseEntity<PatchStationImageResponse> response = stationService.patchStationImage(
				patchStationImageRequest, userLoginId);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getMessage()).isEqualTo("방송국 이미지 변경 성공");
		assertThat(response.getBody().getCode()).isEqualTo("PIS");
	}

	@DisplayName("존재하지않는 방송국-방송국 이미지 변경 실패")
	@Test
	void update_Image_Failed_No_Existed_Station() {
		PatchStationImageRequest patchStationImageRequest = patchStationImageRequest();
		Station station = mock(Station.class);
		Member member = mock(Member.class);
		String userLoginId = "test";
		when(memberRepository.findByLoginId(any())).thenReturn(Optional.of(member));
		when(stationRepository.findByMember(member)).thenThrow(new NoExistedStation());

		assertThatThrownBy(() -> stationService.patchStationImage(patchStationImageRequest,userLoginId)).isInstanceOf(
				NoExistedStation.class);

		NoExistedStation noExistedStation = assertThrows(NoExistedStation.class,
				() -> stationService.patchStationImage(patchStationImageRequest, userLoginId));

		assertThat(noExistedStation.getCode()).isEqualTo("NES");
		assertThat(noExistedStation.getMessage()).isEqualTo("방송국이 존재하지 않습니다.");
	}

	@DisplayName("방송국 설명 변경 성공")
	@Test
	void update_Description_SUCCESS() {
		PatchDescriptionRequest patchDescriptionRequest = descriptionRequest();
		Station station = mock(Station.class);
		Member member = mock(Member.class);
		String userLoginId = "test";
		when(memberRepository.findByLoginId(any())).thenReturn(Optional.of(member));
		when(stationRepository.findByMember(member)).thenReturn(Optional.of(station));
		ResponseEntity<PatchDescriptionResponse> response = stationService.patchDescription(
				patchDescriptionRequest, userLoginId);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getMessage()).isEqualTo("방송국 설명 변경 성공");
		assertThat(response.getBody().getCode()).isEqualTo("PDS");
	}


	@DisplayName("존재하지 않는 방송국 - 방송국 설명 변경 실패")
	@Test
	void update_Description_Failed_NO_Existed_Station() {
		PatchDescriptionRequest patchDescriptionRequest = descriptionRequest();
		Member member = mock(Member.class);
		String userLoginId = "test";
		when(memberRepository.findByLoginId(any())).thenReturn(Optional.of(member));
		when(stationRepository.findByMember(member)).thenThrow(new NoExistedStation());

		assertThatThrownBy(() -> stationService.patchDescription(patchDescriptionRequest,
				userLoginId)).isInstanceOf(
				NoExistedStation.class);

		NoExistedStation response = assertThrows(NoExistedStation.class,
				() -> stationService.patchDescription(descriptionRequest(), userLoginId));

		assertThat(response.getCode()).isEqualTo("NES");
		assertThat(response.getMessage()).isEqualTo("방송국이 존재하지 않습니다.");
	}

	private PatchStationImageRequest patchStationImageRequest() {
		return new PatchStationImageRequest("새로운 이미지");
	}

	private PatchDescriptionRequest descriptionRequest() {
		return new PatchDescriptionRequest("테스트");
	}
}