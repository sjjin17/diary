package todaktodak.domain.user.repository;

import todaktodak.domain.user.domain.SocialType;
import todaktodak.domain.user.domain.User;
import todaktodak.global.exception.CustomException;
import todaktodak.global.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;


    @Test
    void 특정_리프레시_토큰을_갖고있는_사용자_반환() {
        // given
        User expectedUser = userRepository.save(
                User.builder()
                        .socialId("1234")
                        .email("test@test")
                        .imageUrl("imgUrl")
                        .username("user1")
                        .socialType(SocialType.GOOGLE)
                        .refreshToken("refreshToken")
                        .build()
        );

        // when
        User user = userRepository.findByRefreshToken("refreshToken").orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        // then
        assertThat(user).isEqualTo(expectedUser);

    }

}