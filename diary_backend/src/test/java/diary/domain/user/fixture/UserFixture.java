package diary.domain.user.fixture;

import diary.domain.auth.dto.SocialUserInfo;
import diary.domain.user.domain.SocialType;
import diary.domain.user.domain.User;

public class UserFixture {

    private static final String SOCIAL_ID = "1234";
    private static final String EMAIL = "user@test.com";
    private static final String NAME = "username";
    private static final String IMG_URL = "profileImgUrl";
    private static final String REFRESH_TOKEN = "refreshToken";

    public static User createUser(SocialType socialType) {
       return User.builder()
               .socialId(SOCIAL_ID)
               .email(EMAIL)
               .username(NAME)
               .imageUrl(IMG_URL)
               .socialType(socialType)
               .refreshToken(REFRESH_TOKEN)
               .build();
    }




}

