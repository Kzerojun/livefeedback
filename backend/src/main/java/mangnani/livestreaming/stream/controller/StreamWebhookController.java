package mangnani.livestreaming.stream.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangnani.livestreaming.stream.service.StreamService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
@Slf4j
public class StreamWebhookController {

	private final StreamService streamService;

	@PostMapping("/startStreamWebhook")
	private void startStream(@RequestParam("name") String streamKey,
			@RequestParam("addr") String ipAddress) {
		streamService.startStream(streamKey, ipAddress);
	}

	@PostMapping("/endStreamWebhook")
	private void endStream(@RequestParam("name") String streamKey) {
		streamService.endStream(streamKey);
	}
}
