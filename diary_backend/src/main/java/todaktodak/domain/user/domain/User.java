package todaktodak.domain.user.domain;

import todaktodak.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name="users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;

    @Column(name="social_id")
    private String socialId;

    @Column(name="email")
    private String email;

    @Column(name="username")
    private String username;

    @Column(name="social_type")
    private SocialType socialType;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="refresh_token")
    private String refreshToken;


    @Builder
    public User(String socialId, String email, String username, SocialType socialType, String imageUrl, String refreshToken) {
        this.socialId = socialId;
        this.email = email;
        this.username = username;
        this.socialType = socialType;
        this.imageUrl = imageUrl;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }



}
