package todaktodak.domain.diary.repository;

import todaktodak.domain.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findALlByMemberList_User_UserId(Long userId);
}
