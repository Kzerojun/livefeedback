package mangnani.livestreaming.board.dto.object;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mangnani.livestreaming.board.entity.Board;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardItem {

	private Long boardId;
	private String title;
	private String content;
	private int viewCount;
	private String boardTitleImage;
	private LocalDateTime createdAt;

	@Builder
	public BoardItem(Long boardId, String title, String content, int viewCount, String boardTitleImage,
			LocalDateTime createdAt) {
		this.boardId = boardId;
		this.title = title;
		this.content = content;
		this.viewCount = viewCount;
		this.boardTitleImage = boardTitleImage;
		this.createdAt = createdAt;
	}
}
