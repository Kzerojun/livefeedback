package mangnani.livestreaming.broadcast.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.broadcast.dto.request.StartBroadcastRequest;
import mangnani.livestreaming.broadcast.dto.response.GetBroadcastInfoResponse;
import mangnani.livestreaming.broadcast.dto.response.GetBroadcastListResponse;
import mangnani.livestreaming.broadcast.dto.response.StartBroadcastResponse;
import mangnani.livestreaming.broadcast.service.BroadcastService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/broadcast")
public class BroadcastController {

	private final BroadcastService broadcastService;

	@PostMapping("/start")
	public ResponseEntity<StartBroadcastResponse> startBroadcast(
			@RequestBody @Valid StartBroadcastRequest startBroadcastRequest) {
		return broadcastService.startBroadcast(startBroadcastRequest);
	}

	@GetMapping("/category")
	public ResponseEntity<GetBroadcastListResponse> getBroadcasts(
			@RequestParam("category") String category) {
		return broadcastService.getBroadcastsByCategory(category);
	}

	@GetMapping("/info")
	public ResponseEntity<GetBroadcastInfoResponse> getBroadCastInfo(
			@RequestParam("broadcastId") Long broadcastId) {
		return broadcastService.getBroadcastInfo(broadcastId);
	}
}
