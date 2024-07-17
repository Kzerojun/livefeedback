package mangnani.livestreaming.stream.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangnani.livestreaming.stream.dto.response.StreamStatusResponse;
import mangnani.livestreaming.stream.service.StreamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stream")
@RequiredArgsConstructor
@Slf4j
public class StreamController {

	private final StreamService streamService;

	@GetMapping
	public ResponseEntity<StreamStatusResponse> isLiveOn(@RequestParam("streamKey") String streamKey) {
		return streamService.isStreamLiveOn(streamKey);
	}

	@PostMapping("/play")
	public void onPlay(@RequestBody String string) {
		log.info(string);
	}

}
