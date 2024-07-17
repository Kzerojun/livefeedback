package mangnani.livestreaming.broadcast.service.implement;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.broadcast.constant.BroadcastStatus;
import mangnani.livestreaming.broadcast.dto.object.BroadcastInfo;
import mangnani.livestreaming.broadcast.dto.request.StartBroadcastRequest;
import mangnani.livestreaming.broadcast.dto.response.GetBroadcastInfoResponse;
import mangnani.livestreaming.broadcast.dto.response.GetBroadcastListResponse;
import mangnani.livestreaming.broadcast.dto.response.StartBroadcastResponse;
import mangnani.livestreaming.broadcast.entity.Broadcast;
import mangnani.livestreaming.broadcast.exception.NoExistedBroadcast;
import mangnani.livestreaming.broadcast.repository.BroadcastRepository;
import mangnani.livestreaming.broadcast.service.BroadcastService;
import mangnani.livestreaming.file.service.FileService;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BroadcastServiceImpl implements BroadcastService {

	private final BroadcastRepository broadcastRepository;
	private final FileService fileService;
	private final MemberRepository memberRepository;

	@Override
	public ResponseEntity<StartBroadcastResponse> startBroadcast(
			StartBroadcastRequest startBroadcastRequest) {

		Broadcast broadcast = Broadcast.builder()
				.title(startBroadcastRequest.getTitle())
				.attribute(startBroadcastRequest.getAttribute())
				.streamerId(startBroadcastRequest.getStreamerId())
				.streamerNickname(startBroadcastRequest.getStreamerNickname())
				.streamKey(startBroadcastRequest.getStreamKey())
				.category(startBroadcastRequest.getCategory())
				.status(BroadcastStatus.LIVE_ON)
				.build();

		Broadcast savedBroadcast = broadcastRepository.save(broadcast);
		return ResponseEntity.ok().body(StartBroadcastResponse.success(savedBroadcast.getId()));
	}

	@Override
	public ResponseEntity<GetBroadcastInfoResponse> getBroadcastInfo(Long broadcastId) {
		Broadcast broadcast = broadcastRepository.findById(broadcastId)
				.orElseThrow(NoExistedBroadcast::new);

		return ResponseEntity.ok().body(GetBroadcastInfoResponse.success(broadcast.getStreamerId(),
				broadcast.getTitle(), broadcast.getStreamKey()));
	}


	public ResponseEntity<GetBroadcastListResponse> getBroadcastsByCategory(String category) {
		List<Broadcast> broadcasts = getBroadcasts(category);

		List<BroadcastInfo> broadcastInfos = broadcasts.stream()
				.map(this::convertToBroadcastInfo)
				.toList();

		return ResponseEntity.ok().body(GetBroadcastListResponse.success(broadcastInfos));
	}

	private List<Broadcast> getBroadcasts(String category) {
		if (category.equals("ALL")) {
			return broadcastRepository.findByStatus(BroadcastStatus.LIVE_ON);
		} else {
			return broadcastRepository.findByCategoryAndStatus(category, BroadcastStatus.LIVE_ON);
		}
	}

	private BroadcastInfo convertToBroadcastInfo(Broadcast broadcast) {
		String thumbnailUrl = fileService.getThumbnailUrl(broadcast.getStreamKey());
		Member member = memberRepository.findByLoginId(broadcast.getStreamerId())
				.orElseThrow(NoExistedMember::new);

		return BroadcastInfo.builder()
				.title(broadcast.getTitle())
				.streamerId(broadcast.getStreamerId())
				.profileImage(member.getProfileImage())
				.broadcastId(broadcast.getId())
				.thumbnail(thumbnailUrl)
				.build();
	}
}
