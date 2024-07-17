package mangnani.livestreaming.donation.dto.object;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mangnani.livestreaming.donation.repository.projection.MemberView;

@Getter
@NoArgsConstructor
public class MemberItem {

	private String userId;
	private String nickname;
	private String profileImage;

	@Builder
	public MemberItem(MemberView memberView) {
		this.userId = memberView.getLoginId();
		this.nickname = memberView.getNickname();
		this.profileImage = memberView.getProfileImage();
	}

	public static List<MemberItem> getList(List<MemberView> memberViews) {
		return memberViews.stream()
				.map(MemberItem::new)
				.toList();
	}
}
