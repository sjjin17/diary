package todaktodak.domain.user.repository;

import todaktodak.domain.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserUserId(Long userId);
    Boolean existsMemberByUserUserIdAndDiaryDiaryId(Long userId, Long diaryId);

    void deleteByUserUserIdAndDiaryDiaryId(Long userId, Long DiaryId);
}
