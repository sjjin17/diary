package todaktodak.domain.diary.domain;

import todaktodak.domain.user.domain.Member;
import todaktodak.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="diary_id")
    private Long diaryId;

    @Column(name="title")
    private String title;

    @Column(name="is_shared")
    private Boolean isShared;

    @Column(name="thumbnail")
    private String thumbnail;

    @OneToMany(mappedBy = "diary")
    private List<Member> memberList;


    @Builder
    public Diary(String title, Boolean isShared, String thumbnail) {
        this.title = title;
        this.isShared = isShared;
        this.thumbnail = thumbnail;
    }
}
