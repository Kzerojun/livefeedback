package mangnani.livestreaming.broadcast.service;

import mangnani.livestreaming.broadcast.constant.BroadcastCategory;
import mangnani.livestreaming.broadcast.dto.request.StartBroadcastRequest;
import mangnani.livestreaming.broadcast.dto.response.GetBroadcastInfoResponse;
import mangnani.livestreaming.broadcast.dto.response.GetBroadcastListResponse;
import mangnani.livestreaming.broadcast.dto.response.StartBroadcastResponse;
import org.springframework.http.ResponseEntity;

public interface BroadcastService {

	ResponseEntity<StartBroadcastResponse> startBroadcast(
			StartBroadcastRequest startBroadcastRequest);

	ResponseEntity<GetBroadcastInfoResponse> getBroadcastInfo(Long broadcastId);

	ResponseEntity<GetBroadcastListResponse> getBroadcastsByCategory(BroadcastCategory category);

}
