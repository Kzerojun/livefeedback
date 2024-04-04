package mangnani.livestreaming.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mangnani.livestreaming.member.constant.Role;
import mangnani.livestreaming.member.constant.Status;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String loginId;

	private String password;

	private String nickname;

	@CreatedDate
	private LocalDateTime createdAt;

	@Enumerated(value = EnumType.STRING)
	private Status status;

	@Enumerated(value = EnumType.STRING)
	private Role role;

	@Column(name = "star_candy_amount")
	private Integer starCandyAmount;

	@Column(name = "verification")
	private Boolean verification;

	@Builder
	public Member(String loginId, String password, String nickname) {
		this.loginId = loginId;
		this.password = password;
		this.nickname = nickname;
		this.status = Status.ACTIVATE;
		this.role = Role.USER;
		this.starCandyAmount = 0;
		this.verification = false;
	}

}