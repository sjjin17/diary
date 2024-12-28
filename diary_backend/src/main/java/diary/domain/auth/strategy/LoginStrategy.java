package diary.domain.auth.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import diary.domain.auth.dto.SocialUserInfo;
import diary.domain.user.domain.SocialType;

public interface LoginStrategy {
    SocialUserInfo login(String code, SocialType socialType) throws JsonProcessingException;

    //private String getAccessToken(String code, SocialType socialType);
}
