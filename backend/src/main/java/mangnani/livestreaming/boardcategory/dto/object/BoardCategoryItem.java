package mangnani.livestreaming.boardcategory.dto.object;


import java.util.List;
import lombok.Builder;
import lombok.Getter;
import mangnani.livestreaming.boardcategory.entity.BoardCategory;

@Getter
public class BoardCategoryItem {

	private final String category;
	private final String description;

	@Builder
	private BoardCategoryItem(String category, String description) {
		this.category = category;
		this.description = description;
	}

	public static List<BoardCategoryItem> getList(List<BoardCategory> boardCategories) {
		 return boardCategories.stream()
				.map(boardCategory -> BoardCategoryItem.builder()
						.category(boardCategory.getCategory())
						.description(boardCategory.getDescription())
						.build()
				).toList();
	}

}
