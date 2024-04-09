package mangnani.livestreaming.station.service;

import mangnani.livestreaming.station.dto.request.PatchDescriptionRequest;
import mangnani.livestreaming.station.dto.request.PatchStationImageRequest;
import mangnani.livestreaming.station.dto.response.PatchDescriptionResponse;
import mangnani.livestreaming.station.dto.response.PatchStationImageResponse;
import org.springframework.http.ResponseEntity;

public interface StationService {

	ResponseEntity<PatchStationImageResponse> patchStationImage(
			PatchStationImageRequest patchStationImageRequest, String userLoginId);

	ResponseEntity<PatchDescriptionResponse> patchDescription(
			PatchDescriptionRequest patchDescription, String userLoginId);

}
