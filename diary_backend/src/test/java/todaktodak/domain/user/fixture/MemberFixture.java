package todaktodak.domain.user.fixture;

import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.user.domain.Member;
import todaktodak.domain.user.domain.User;

public class MemberFixture {
    public static final Member createMember(User user, Diary diary) {
        return Member.builder()
                .diary(diary)
                .user(user)
                .turn(1)
                .isHost(true)
                .build();

    }
}
