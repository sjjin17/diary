package todaktodak.domain.user.dto.response;

import lombok.Getter;
import todaktodak.domain.diary.domain.Diary;
import lombok.Builder;

@Getter
public class MyDiaryResponseDto {
    Long diaryId;
    String title;
    Boolean isShared;
    String thumbnail;



    @Builder
    public MyDiaryResponseDto(Long diaryId, String title, Boolean isShared, String thumbnail) {
        this.diaryId = diaryId;
        this.title = title;
        this.isShared = isShared;
        this.thumbnail = thumbnail;
    }


    public static MyDiaryResponseDto from(Diary diary) {
        return MyDiaryResponseDto.builder()
                .diaryId(diary.getDiaryId())
                .title(diary.getTitle())
                .isShared(diary.getIsShared())
                .thumbnail(diary.getThumbnail())
                .build();
    }



}

