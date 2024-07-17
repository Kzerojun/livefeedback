package mangnani.livestreaming.favorite.controller;

import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.favorite.dto.request.GetIsFavoriteRequest;
import mangnani.livestreaming.favorite.dto.request.PostFavoriteRequest;
import mangnani.livestreaming.favorite.dto.response.GetIsFavoriteResponse;
import mangnani.livestreaming.favorite.dto.response.PostFavoriteResponse;
import mangnani.livestreaming.favorite.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {

	private final FavoriteService favoriteService;

	@PostMapping
	public ResponseEntity<PostFavoriteResponse> postFavorite(
			@RequestBody PostFavoriteRequest postFavoriteRequest) {
		return favoriteService.postFavorite(postFavoriteRequest);
	}

	@GetMapping()
	public ResponseEntity<GetIsFavoriteResponse> getIsFavorite(
			@RequestBody GetIsFavoriteRequest getIsFavoriteRequest) {
		return favoriteService.getIsFavorite(getIsFavoriteRequest);
	}
}
