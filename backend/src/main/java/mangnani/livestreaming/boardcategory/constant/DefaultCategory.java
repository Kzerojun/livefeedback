package mangnani.livestreaming.boardcategory.constant;

import lombok.Getter;

@Getter
public enum DefaultCategory {
	ANNOUNCEMENT("공지사항", "방송국의 공지사항을 위한 게시판"),
	FREE_BOARD("자유게시판", "자유롭게 의견을 나누는 게시판");

	private final String name;
	private final String description;

	DefaultCategory(String name, String description) {
		this.name = name;
		this.description = description;
	}
}

