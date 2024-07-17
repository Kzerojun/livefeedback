package mangnani.livestreaming.boardcategory.service.implement;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mangnani.livestreaming.boardcategory.dto.request.PostBoardCategoryRequest;
import mangnani.livestreaming.boardcategory.dto.response.GetBoardCategoriesResponse;
import mangnani.livestreaming.boardcategory.dto.response.PostBoardCategoryResponse;
import mangnani.livestreaming.boardcategory.entity.BoardCategory;
import mangnani.livestreaming.boardcategory.repository.BoardCategoryRepository;
import mangnani.livestreaming.boardcategory.service.BoardCategoryService;
import mangnani.livestreaming.member.entity.Member;
import mangnani.livestreaming.member.exception.NoExistedMember;
import mangnani.livestreaming.member.repository.MemberRepository;
import mangnani.livestreaming.station.entity.Station;
import mangnani.livestreaming.station.exception.NoExistedStation;
import mangnani.livestreaming.station.repository.StationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardCategoryServiceImpl implements BoardCategoryService {

	private final BoardCategoryRepository boardCategoryRepository;
	private final MemberRepository memberRepository;
	private final StationRepository stationRepository;

	@Override
	public ResponseEntity<PostBoardCategoryResponse> postBoardCategory(
			PostBoardCategoryRequest postBoardCategoryRequest, String memberLoginId) {

		Member member = memberRepository.findByLoginId(memberLoginId)
				.orElseThrow(NoExistedMember::new);
		Station station = stationRepository.findByMember(member).orElseThrow(NoExistedStation::new);

		BoardCategory boardCategory = BoardCategory.builder()
				.category(postBoardCategoryRequest.getName())
				.description(postBoardCategoryRequest.getDescription())
				.station(station)
				.build();

		boardCategoryRepository.save(boardCategory);

		return ResponseEntity.status(HttpStatus.CREATED).body(PostBoardCategoryResponse.success());
	}

	@Override
	public ResponseEntity<GetBoardCategoriesResponse> getBoardCategories(String memberLoginId) {
		Member member = memberRepository.findMemberByLoginIdWithStationAndBoardCategories(memberLoginId)
				.orElseThrow(NoExistedMember::new);
		List<BoardCategory> boardCategories = member.getStation().getBoardCategories();
		return ResponseEntity.ok().body(GetBoardCategoriesResponse.success(boardCategories));
	}
}
