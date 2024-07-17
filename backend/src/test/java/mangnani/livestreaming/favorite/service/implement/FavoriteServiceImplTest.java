package mangnani.livestreaming.favorite.service.implement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import mangnani.livestreaming.favorite.dto.request.PostFavoriteRequest;
import mangnani.livestreaming.favorite.dto.response.PostFavoriteResponse;
import mangnani.livestreaming.favorite.repository.FavoriteRepository;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

	@Mock
	private FavoriteRepository favoriteRepository;

	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private FavoriteServiceImpl favoriteService;

	@DisplayName("즐겨찾기 추가 성공")
	@Test
	void postFavorite_Success() {
		PostFavoriteRequest postFavoriteRequest = postFavoriteRequest();
		when(memberRepository.existsByLoginId("test1")).thenReturn(true);
		when(memberRepository.existsByLoginId("test2")).thenReturn(true);

		ResponseEntity<PostFavoriteResponse> response = favoriteService.postFavorite(
				postFavoriteRequest);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getCode()).isEqualTo("SU");
		assertThat(response.getBody().getMessage()).isEqualTo("성공");
	}

	@DisplayName("즐겨찾기 추가 실패, 팔로워 회원 존재X")
	@Test
	void postFavorite_Failed_FollowerId_No_Existed() {
		PostFavoriteRequest postFavoriteRequest = postFavoriteRequest();
		when(memberRepository.existsByLoginId("test1")).thenReturn(true);
		when(memberRepository.existsByLoginId("test2")).thenReturn(false);

		assertThatThrownBy(() -> favoriteService.postFavorite(postFavoriteRequest)).isInstanceOf(
				NoExistedMember.class);

		NoExistedMember noExistedMember = assertThrows(NoExistedMember.class,
				() -> favoriteService.postFavorite(postFavoriteRequest));

		assertThat(noExistedMember.getCode()).isEqualTo("NEM");
		assertThat(noExistedMember.getMessage()).isEqualTo("멤버가 존재하지 않습니다.");
	}

	@DisplayName("즐겨찾기 추가 실패, 팔로위 회원 존재X")
	@Test
	void postFavorite_Failed_FolloweeId_No_Existed() {
		PostFavoriteRequest postFavoriteRequest = postFavoriteRequest();
		when(memberRepository.existsByLoginId("test1")).thenReturn(false);

		assertThatThrownBy(() -> favoriteService.postFavorite(postFavoriteRequest)).isInstanceOf(
				NoExistedMember.class);

		NoExistedMember noExistedMember = assertThrows(NoExistedMember.class,
				() -> favoriteService.postFavorite(postFavoriteRequest));

		assertThat(noExistedMember.getCode()).isEqualTo("NEM");
		assertThat(noExistedMember.getMessage()).isEqualTo("멤버가 존재하지 않습니다.");
	}

	private PostFavoriteRequest postFavoriteRequest() {
		PostFavoriteRequest postFavoriteRequest = new PostFavoriteRequest();
		postFavoriteRequest.setFolloweeId("test1");
		postFavoriteRequest.setFollowerId("test2");
		return postFavoriteRequest;
	}

}