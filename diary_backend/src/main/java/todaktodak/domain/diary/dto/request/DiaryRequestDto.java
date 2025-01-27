package todaktodak.domain.diary.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.user.domain.Member;

import java.util.List;

@Getter
public class DiaryRequestDto {

    private String title;

    private Boolean isShared;


    public Diary toEntity(String imageUrl) {
        return Diary.builder()
                .title(title)
                .isShared(isShared)
                .thumbnail(imageUrl)
                .build();
    }
}
