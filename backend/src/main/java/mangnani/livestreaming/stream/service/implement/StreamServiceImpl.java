package mangnani.livestreaming.stream.service.implement;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.broadcast.constant.BroadcastStatus;
import mangnani.livestreaming.broadcast.entity.Broadcast;
import mangnani.livestreaming.broadcast.exception.NoExistedBroadcast;
import mangnani.livestreaming.broadcast.repository.BroadcastRepository;
import mangnani.livestreaming.stream.constant.StreamStatus;
import mangnani.livestreaming.stream.dto.response.StreamStatusResponse;
import mangnani.livestreaming.stream.entity.Stream;
import mangnani.livestreaming.stream.exception.NoExistedLiveStream;
import mangnani.livestreaming.stream.exception.StreamAlreadyActiveException;
import mangnani.livestreaming.stream.repository.StreamRepository;
import mangnani.livestreaming.stream.service.StreamService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StreamServiceImpl implements StreamService {

	private final StreamRepository streamRepository;
	private final BroadcastRepository broadcastRepository;

	@Override
	public void startStream(String streamKey,String ipAddress) {

		Optional<Stream> stream = streamRepository.findByStreamKeyAndStreamStatus(
				streamKey, StreamStatus.LIVE_ON);

		if (stream.isPresent()) {
			throw StreamAlreadyActiveException.alreadyActiveException();
		}

		Stream activeStream = Stream.builder()
				.streamKey(streamKey)
				.ipAddress(ipAddress)
				.streamStatus(StreamStatus.LIVE_ON)
				.build();

		streamRepository.save(activeStream);
	}

	@Override
	@Transactional
	public void endStream(String streamKey) {
		Stream activeStream = streamRepository.findByStreamKeyAndStreamStatus(
				streamKey, StreamStatus.LIVE_ON).orElseThrow(NoExistedLiveStream::new);
		Broadcast broadcast = broadcastRepository.findBroadcastByStreamKey(streamKey)
				.orElseThrow(NoExistedBroadcast::new);

		broadcast.endBroadCast(BroadcastStatus.LIVE_OFF);
		activeStream.updateLiveEnd(StreamStatus.LIVE_OFF);
	}

	@Override
	public ResponseEntity<StreamStatusResponse> isStreamLiveOn(
			String streamKey) {
		Optional<Stream> stream = streamRepository.findByStreamKeyAndStreamStatus(
				streamKey,
				StreamStatus.LIVE_ON);

		if (stream.isEmpty()) {
			return ResponseEntity.ok().body(StreamStatusResponse.success(StreamStatus.LIVE_OFF));
		}

		return ResponseEntity.ok().body(StreamStatusResponse.success(StreamStatus.LIVE_ON));
	}
}
