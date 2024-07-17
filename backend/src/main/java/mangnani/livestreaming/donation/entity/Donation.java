package mangnani.livestreaming.donation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mangnani.livestreaming.member.entity.Member;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Donation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "donation_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "donor_member_id")
	private Member donor;

	@ManyToOne
	@JoinColumn(name = "recipient_member_id")
	private Member recipient;

	@Column(name="star_candy_amount")
	private int starCandyAmount;

	@Column(name = "message")
	private String message;

	@CreatedDate
	@Column(name = "donated_at")
	private LocalDateTime donatedAt;

	@Builder
	public Donation(Member donor, Member recipient, int starCandyAmount,String message) {
		this.donor = donor;
		this.recipient = recipient;
		this.starCandyAmount = starCandyAmount;
		this.message = message;
	}
}
