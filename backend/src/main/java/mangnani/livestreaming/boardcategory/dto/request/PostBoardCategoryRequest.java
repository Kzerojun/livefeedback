package mangnani.livestreaming.boardcategory.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostBoardCategoryRequest {

	@NotBlank(message = "제목을 입력해주세요.")
	private String name;
	@NotBlank(message = "설명을 입력해주세요.")
	private String description;

}
