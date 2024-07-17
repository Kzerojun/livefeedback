package mangnani.livestreaming.boardcategory.service;

import mangnani.livestreaming.boardcategory.dto.request.PostBoardCategoryRequest;
import mangnani.livestreaming.boardcategory.dto.response.GetBoardCategoriesResponse;
import mangnani.livestreaming.boardcategory.dto.response.PostBoardCategoryResponse;
import org.springframework.http.ResponseEntity;

public interface BoardCategoryService {

	ResponseEntity<GetBoardCategoriesResponse> getBoardCategories(String memberLoginId);
	ResponseEntity<PostBoardCategoryResponse> postBoardCategory(PostBoardCategoryRequest postBoardCategoryRequest,String memberLoginId);
}
