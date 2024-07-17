package mangnani.livestreaming.stream.service;

import mangnani.livestreaming.stream.dto.response.StreamStatusResponse;
import org.springframework.http.ResponseEntity;

public interface StreamService {

	void startStream(String streamKey, String ipAddress);

	void endStream(String streamKey);

	ResponseEntity<StreamStatusResponse> isStreamLiveOn(String streamKey);

}
