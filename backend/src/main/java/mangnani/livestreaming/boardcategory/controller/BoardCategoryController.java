package mangnani.livestreaming.boardcategory.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.boardcategory.dto.request.PostBoardCategoryRequest;
import mangnani.livestreaming.boardcategory.dto.response.GetBoardCategoriesResponse;
import mangnani.livestreaming.boardcategory.dto.response.PostBoardCategoryResponse;
import mangnani.livestreaming.boardcategory.service.BoardCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boardcategory")
@RequiredArgsConstructor
public class BoardCategoryController {

	private final BoardCategoryService boardCategoryService;

	@GetMapping("/{userLoginId}")
	public ResponseEntity<GetBoardCategoriesResponse> getBoardCategories(
			@PathVariable String userLoginId) {
		return boardCategoryService.getBoardCategories(userLoginId);
	}
	@PostMapping("/{userLoginId}")
	public ResponseEntity<PostBoardCategoryResponse> postBoardCategory(@Valid @RequestBody PostBoardCategoryRequest postBoardCategoryRequest,
			@PathVariable String userLoginId) {
		return boardCategoryService.postBoardCategory(postBoardCategoryRequest,userLoginId);
	}
}
