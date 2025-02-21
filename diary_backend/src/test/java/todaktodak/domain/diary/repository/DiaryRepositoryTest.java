package todaktodak.domain.diary.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.diary.fixture.DiaryFixture;
import todaktodak.domain.user.domain.Member;
import todaktodak.domain.user.domain.SocialType;
import todaktodak.domain.user.domain.User;
import todaktodak.domain.user.fixture.MemberFixture;
import todaktodak.domain.user.fixture.UserFixture;
import todaktodak.domain.user.repository.MemberRepository;
import todaktodak.domain.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DiaryRepositoryTest {
    @Autowired
    DiaryRepository diaryRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void 유저_아이디로_참여중인_다이어리_목록_찾기() {
        // given
        User user1 = UserFixture.createUser(SocialType.GOOGLE);
        User user2 = UserFixture.createUser(SocialType.GOOGLE);
        Diary diary1 = DiaryFixture.createDiary("일기장1", false);
        Diary diary2 = DiaryFixture.createDiary("일기장2", true);
        Diary diary3 = DiaryFixture.createDiary("일기장3", true);
        Diary diary4 = DiaryFixture.createDiary("일기장4", true);
        Member member1 = MemberFixture.createMember(user1, diary1);
        Member member2 = MemberFixture.createMember(user1, diary2);
        Member member3 = MemberFixture.createMember(user1, diary3);
        Member member4 = MemberFixture.createMember(user2, diary3);
        Member member5 = MemberFixture.createMember(user2, diary4);

        userRepository.saveAll(List.of(user1, user2));
        diaryRepository.saveAll(List.of(diary1, diary2, diary3, diary4));
        memberRepository.saveAll(List.of(member1, member2, member3, member4, member5));


        // when
        List<Diary> diaryList = diaryRepository.findALlByMemberList_User_Id(user1.getId());

        // then
        assertThat(diaryList.size()).isEqualTo(3);
    }
}