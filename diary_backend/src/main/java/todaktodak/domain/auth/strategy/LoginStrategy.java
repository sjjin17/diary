package todaktodak.domain.auth.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import todaktodak.domain.auth.dto.SocialUserInfo;
import todaktodak.domain.user.domain.SocialType;

public interface LoginStrategy {
    SocialUserInfo login(String code, SocialType socialType) throws JsonProcessingException;

    //private String getAccessToken(String code, SocialType socialType);
}
