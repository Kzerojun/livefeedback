package mangnani.livestreaming.favorite.service;

import mangnani.livestreaming.favorite.dto.request.GetIsFavoriteRequest;
import mangnani.livestreaming.favorite.dto.request.PostFavoriteRequest;
import mangnani.livestreaming.favorite.dto.response.GetIsFavoriteResponse;
import mangnani.livestreaming.favorite.dto.response.PostFavoriteResponse;
import org.springframework.http.ResponseEntity;

public interface FavoriteService {

	ResponseEntity<PostFavoriteResponse> postFavorite(PostFavoriteRequest postFavoriteRequest);

	ResponseEntity<GetIsFavoriteResponse> getIsFavorite(GetIsFavoriteRequest getIsFavoriteRequest);
}
