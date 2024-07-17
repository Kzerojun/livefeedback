package mangnani.livestreaming.board.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.board.dto.request.PostBoardRequest;
import mangnani.livestreaming.board.dto.response.GetBoardDetailResponse;
import mangnani.livestreaming.board.dto.response.GetBoardListResponse;
import mangnani.livestreaming.board.dto.response.PostBoardResponse;
import mangnani.livestreaming.board.service.BoardService;
import mangnani.livestreaming.global.dto.ResponseDto;
import mangnani.livestreaming.global.dto.ResponseMessage;
import mangnani.livestreaming.member.dto.response.GetSignInMemberResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.http.Path;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;

	@PostMapping("/post")
	public ResponseEntity<PostBoardResponse> postBoard(@RequestBody @Valid
	PostBoardRequest postBoardRequest, @AuthenticationPrincipal @NotNull User user) {
		return boardService.postBoard(postBoardRequest, user.getUsername());
	}

	@GetMapping("/{userLoginId}")
	public ResponseEntity<GetBoardListResponse> getBoard(@PathVariable String userLoginId,
			@RequestParam String category,
			@PageableDefault(size = 5, sort = "createdAt", direction = Direction.DESC) Pageable pageable
	) {
		return boardService.getBoardList(userLoginId, category, pageable);
	}

	@GetMapping("/detail")
	public ResponseEntity<GetBoardDetailResponse> getBoardDetail(@RequestParam Long boardId) {
		return boardService.getBoardDetail(boardId);
	}

}
