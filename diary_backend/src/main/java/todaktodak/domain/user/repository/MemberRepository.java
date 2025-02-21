package todaktodak.domain.user.repository;

import todaktodak.domain.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserId(Long userId);
    Boolean existsMemberByUserIdAndDiaryId(Long userId, Long diaryId);

    void deleteByUserIdAndDiaryId(Long userId, Long DiaryId);
}
