package mangnani.livestreaming.station.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import mangnani.livestreaming.station.dto.request.PatchDescriptionRequest;
import mangnani.livestreaming.station.dto.request.PatchStationImageRequest;
import mangnani.livestreaming.station.dto.response.GetStationResponse;
import mangnani.livestreaming.station.dto.response.PatchDescriptionResponse;
import mangnani.livestreaming.station.dto.response.PatchStationImageResponse;
import mangnani.livestreaming.station.entity.Station;
import mangnani.livestreaming.station.exception.NoExistedStation;
import mangnani.livestreaming.station.repository.StationRepository;
import mangnani.livestreaming.station.service.StationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

	private final StationRepository stationRepository;
	private final MemberRepository memberRepository;

	@Override
	public ResponseEntity<GetStationResponse> getStation(String userId) {
		Member member = memberRepository.findMemberWithStationByLoginId(userId)
				.orElseThrow(NoExistedMember::new);

		return ResponseEntity.ok()
				.body(GetStationResponse.success(member.getStation().getDescription(), member.getStation().getImage()));
	}

	@Override
	@Transactional
	public ResponseEntity<PatchStationImageResponse> patchStationImage(
			PatchStationImageRequest patchStationImageRequest, String userLoginId) {

		Member member = memberRepository.findByLoginId(userLoginId)
				.orElseThrow(NoExistedMember::new);
		Station station = stationRepository.findByMember(member).orElseThrow(NoExistedStation::new);

		station.updateImage(patchStationImageRequest.getImage());

		return ResponseEntity.ok().body(PatchStationImageResponse.success());
	}

	@Override
	@Transactional
	public ResponseEntity<PatchDescriptionResponse> patchDescription(
			PatchDescriptionRequest patchDescription, String userLoginId) {
		Member member = memberRepository.findMemberWithStationByLoginId(userLoginId)
				.orElseThrow(NoExistedMember::new);

		member.getStation().updateDescription(patchDescription.getDescription());

		return ResponseEntity.ok().body(PatchDescriptionResponse.success());
	}
}
