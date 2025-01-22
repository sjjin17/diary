package todaktodak.domain.user.domain;

import lombok.Getter;
import todaktodak.domain.diary.domain.Diary;
import jakarta.persistence.*;

@Entity
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long memberId;

    @Column(name = "turn")
    private int turn;

    @Column(name = "is_host")
    private Boolean isHost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;
}
