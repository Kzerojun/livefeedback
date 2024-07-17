package mangnani.livestreaming.favorite.service.implement;

import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.favorite.dto.request.GetIsFavoriteRequest;
import mangnani.livestreaming.favorite.dto.request.PostFavoriteRequest;
import mangnani.livestreaming.favorite.dto.response.GetIsFavoriteResponse;
import mangnani.livestreaming.favorite.dto.response.PostFavoriteResponse;
import mangnani.livestreaming.favorite.entity.Favorite;
import mangnani.livestreaming.favorite.repository.FavoriteRepository;
import mangnani.livestreaming.favorite.service.FavoriteService;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

	private final FavoriteRepository favoriteRepository;
	private final MemberRepository memberRepository;

	@Override
	public ResponseEntity<PostFavoriteResponse> postFavorite(
			PostFavoriteRequest postFavoriteRequest) {

		if (!memberRepository.existsByLoginId(postFavoriteRequest.getFolloweeId())
				|| !memberRepository.existsByLoginId(
				postFavoriteRequest.getFollowerId())) {
			throw new NoExistedMember();
		}

		Favorite favorite = Favorite.builder()
				.followerId(postFavoriteRequest.getFollowerId())
				.followeeId(postFavoriteRequest.getFolloweeId())
				.build();

		favoriteRepository.save(favorite);

		return ResponseEntity.ok().body(PostFavoriteResponse.success());
	}

	@Override
	public ResponseEntity<GetIsFavoriteResponse> getIsFavorite(
			GetIsFavoriteRequest getIsFavoriteRequest) {

		if (!memberRepository.existsByLoginId(getIsFavoriteRequest.getFolloweeId())
				|| !memberRepository.existsByLoginId(
				getIsFavoriteRequest.getFollowerId())) {
			throw new NoExistedMember();
		}

		boolean check = favoriteRepository.existsByFolloweeIdAndFollowerId(
				getIsFavoriteRequest.getFolloweeId(),
				getIsFavoriteRequest.getFollowerId());

		return ResponseEntity.ok().body(GetIsFavoriteResponse.success(check));
	}
}
