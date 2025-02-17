package todaktodak.domain.post.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.user.domain.User;
import todaktodak.global.common.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "written_date")
    private LocalDate writtenDate;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "weather")
    private Weather weather;

    @Column(name = "emotion")
    private Emotion emotion;

    @Column(name = "is_published")
    private Boolean isPublished;

    @Formula("(select count(*) from post_like pl where pl.post_id=id)")
    @Column(name = "like_count")
    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="diary_id")
    private Diary diary;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostLike> postLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostImage> postImageList = new ArrayList<>();


    @Builder
    public Post(LocalDate writtenDate, String title, String content, Weather weather, Emotion emotion, Boolean isPublished, User user, Diary diary) {
        this.writtenDate = writtenDate;
        this.title = title;
        this.content = content;
        this.weather = weather;
        this.emotion = emotion;
        this.isPublished = isPublished;
        this.user = user;
        this.diary = diary;
    }




}
