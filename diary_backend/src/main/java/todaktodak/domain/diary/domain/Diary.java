package todaktodak.domain.diary.domain;

import lombok.ToString;
import todaktodak.domain.post.domain.Post;
import todaktodak.domain.user.domain.Member;
import todaktodak.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="diary_id")
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="is_shared", columnDefinition = "TINYINT(1)")
    private Boolean isShared;

    @Column(name="thumbnail")
    private String thumbnail;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<Member> memberList = new ArrayList<>();

    @OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE)
    private List<Post> postList = new ArrayList<>();

    @Builder
    public Diary(String title, Boolean isShared, String thumbnail) {
        this.title = title;
        this.isShared = isShared;
        this.thumbnail = thumbnail;
    }

    public void addMember(Member member) {
        memberList.add(member);
    }

    public void removeMember(Member member) {
        memberList.remove(member);
    }

    public void updateDiary(String title, String thumbnail) {
        this.title = title;
        this.thumbnail = thumbnail;
    }
}
