package todaktodak.domain.diary.fixture;

import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.user.dto.response.MyDiaryResponseDto;

public class DiaryFixture {


    public static final Diary createDiary(String title, Boolean isShared) {
        return Diary.builder()
                .title(title)
                .isShared(isShared)
                .thumbnail("image")
                .build();
    }

    public static final MyDiaryResponseDto MOCK_MY_DIARY_RESPONSE_DTO = MyDiaryResponseDto.builder()
            .diaryId(1L)
            .isShared(true)
            .thumbnail("image")
            .title("일기장")
            .build();



}
