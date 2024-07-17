package mangnani.livestreaming.station.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangnani.livestreaming.station.dto.request.PatchDescriptionRequest;
import mangnani.livestreaming.station.dto.request.PatchStationImageRequest;
import mangnani.livestreaming.station.dto.response.GetStationResponse;
import mangnani.livestreaming.station.dto.response.PatchDescriptionResponse;
import mangnani.livestreaming.station.dto.response.PatchStationImageResponse;
import mangnani.livestreaming.station.service.StationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/station")
@RequiredArgsConstructor
@Slf4j
public class StationController {

	private final StationService stationService;

	@GetMapping("/{userId}")
	public ResponseEntity<GetStationResponse> getStation(@PathVariable String userId) {
		return stationService.getStation(userId);
	}

	@PatchMapping("/description")
	public ResponseEntity<PatchDescriptionResponse> patchDescription(
			@RequestBody PatchDescriptionRequest patchDescriptionRequest,
			@AuthenticationPrincipal User user) {
		return stationService.patchDescription(patchDescriptionRequest, user.getUsername());
	}

	@PatchMapping("/image")
	public ResponseEntity<PatchStationImageResponse> patchImage(
			@RequestBody PatchStationImageRequest patchStationImageRequest,
			@AuthenticationPrincipal User user) {
		return stationService.patchStationImage(patchStationImageRequest, user.getUsername());
	}
}
