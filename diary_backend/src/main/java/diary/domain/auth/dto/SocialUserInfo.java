package diary.domain.auth.dto;

import diary.domain.user.domain.SocialType;
import lombok.Builder;


@Builder
public record SocialUserInfo(String socialId, String email, String name, String imageUrl, SocialType socialType) {
}
