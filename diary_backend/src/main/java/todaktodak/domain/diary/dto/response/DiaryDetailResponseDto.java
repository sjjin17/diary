package todaktodak.domain.diary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.user.domain.Member;

import java.util.List;

@Getter
public class DiaryDetailResponseDto {

    private Long diaryId;
    private String title;
    private Boolean isShared;
    private String thumbnail;



    @Builder
    public DiaryDetailResponseDto(Long diaryId, String title, Boolean isShared, String thumbnail) {
        this.diaryId = diaryId;
        this.title = title;
        this.isShared = isShared;
        this.thumbnail = thumbnail;
    }




    public static DiaryDetailResponseDto from(Diary diary) {
        return DiaryDetailResponseDto.builder()
                .diaryId(diary.getDiaryId())
                .title(diary.getTitle())
                .isShared(diary.getIsShared())
                .thumbnail(diary.getThumbnail())
                .build();
    }
}
