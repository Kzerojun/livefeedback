package mangnani.livestreaming.board.service;

import mangnani.livestreaming.board.dto.request.PostBoardRequest;
import mangnani.livestreaming.board.dto.response.GetBoardDetailResponse;
import mangnani.livestreaming.board.dto.response.GetBoardListResponse;
import mangnani.livestreaming.board.dto.response.PostBoardResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface BoardService {

	ResponseEntity<PostBoardResponse> postBoard(PostBoardRequest postBoardRequest,String loginId);

	ResponseEntity<GetBoardListResponse> getBoardList(String userLoginId, String category, Pageable pageable);

	ResponseEntity<GetBoardDetailResponse> getBoardDetail(Long boardId);

}
