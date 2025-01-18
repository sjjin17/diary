package diary.domain.user.repository;

import diary.domain.user.domain.SocialType;
import diary.domain.user.domain.User;
import diary.domain.user.fixture.UserFixture;
import diary.global.exception.CustomException;
import diary.global.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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