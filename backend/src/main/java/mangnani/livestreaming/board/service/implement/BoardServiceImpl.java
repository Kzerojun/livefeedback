package mangnani.livestreaming.board.service.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.board.dto.object.BoardItem;
import mangnani.livestreaming.board.dto.request.PostBoardRequest;
import mangnani.livestreaming.board.dto.response.GetBoardDetailResponse;
import mangnani.livestreaming.board.dto.response.GetBoardListResponse;
import mangnani.livestreaming.board.dto.response.PostBoardResponse;
import mangnani.livestreaming.board.entity.Board;
import mangnani.livestreaming.board.exception.NoExistedBoard;
import mangnani.livestreaming.board.repository.BoardRepository;
import mangnani.livestreaming.board.service.BoardService;
import mangnani.livestreaming.boardcategory.entity.BoardCategory;
import mangnani.livestreaming.boardcategory.exception.NoExistedBoardCategory;
import mangnani.livestreaming.boardcategory.repository.BoardCategoryRepository;
import mangnani.livestreaming.image.entity.Image;
import mangnani.livestreaming.image.repository.ImageRepository;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import mangnani.livestreaming.station.entity.Station;
import mangnani.livestreaming.station.exception.NoExistedStation;
import mangnani.livestreaming.station.repository.StationRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;
	private final BoardCategoryRepository boardCategoryRepository;
	private final StationRepository stationRepository;
	private final MemberRepository memberRepository;
	private final ImageRepository imageRepository;

	@Override
	public ResponseEntity<PostBoardResponse> postBoard(PostBoardRequest postBoardRequest,String loginId) {
		if (!memberRepository.existsByLoginId(loginId)) {
			throw new NoExistedMember();
		}
		Station station = stationRepository.findByMemberLoginId(loginId)
				.orElseThrow(NoExistedStation::new);

		BoardCategory boardCategory = boardCategoryRepository.findByCategoryAndStation(
				postBoardRequest.getCategory(), station).orElseThrow(NoExistedBoardCategory::new);

		Board board = Board.builder().title(postBoardRequest.getTitle())
				.content(postBoardRequest.getContent())
				.boardCategory(boardCategory)
				.build();
		boardRepository.save(board);

		Long boardId = board.getId();
		List<Image> images = postBoardRequest.getBoardImageList()
				.stream()
				.map(image ->
						Image.builder()
						.boardNumber(boardId)
						.image(image)
						.build()
				).toList();
		imageRepository.saveAll(images);

		return ResponseEntity.status(HttpStatus.CREATED).body(PostBoardResponse.success());
	}

	@Override
	public ResponseEntity<GetBoardListResponse> getBoardList(String userLoginId, String category,
			Pageable pageable) {

		Station station = stationRepository.findByMemberLoginId(userLoginId)
				.orElseThrow(NoExistedStation::new);

		BoardCategory boardCategory = boardCategoryRepository.findByCategoryAndStation(
				category, station).orElseThrow(NoExistedBoardCategory::new);

		List<Board> boards = boardRepository.findBoardsByBoardCategory(boardCategory,pageable);
		int totalBoardSize = boardRepository.countByBoardCategory(boardCategory);

		List<BoardItem> boardItemList = boards.stream()
				.map(board -> {
					List<Image> images = imageRepository.findByBoardNumber(board.getId());
					String imageUrl = images.isEmpty() ? null : images.get(0).getImage();
					return BoardItem.builder()
							.boardId(board.getId())
							.title(board.getTitle())
							.boardTitleImage(imageUrl)
							.viewCount(board.getViewCount())
							.createdAt(board.getCreatedAt())
							.build();
				})
				.toList();

		return ResponseEntity.ok().body(GetBoardListResponse.success(boardItemList,totalBoardSize));
	}

	@Override
	public ResponseEntity<GetBoardDetailResponse> getBoardDetail(Long boardId) {
		Board board = boardRepository.findById(boardId).orElseThrow(NoExistedBoard::new);
		List<Image> images = imageRepository.findByBoardNumber(boardId);

		List<String> imageUrls = images.stream()
				.map(Image::getImage)
				.toList();

		return ResponseEntity.ok().body(GetBoardDetailResponse.success(board.getTitle(),
				board.getContent(),imageUrls));
	}
}
