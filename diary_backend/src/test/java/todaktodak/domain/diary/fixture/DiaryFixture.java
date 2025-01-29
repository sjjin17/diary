package todaktodak.domain.diary.fixture;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.diary.dto.request.DiaryRequestDto;
import todaktodak.domain.diary.dto.response.DiaryDetailResponseDto;
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


    public static final DiaryRequestDto MOCK_DIARY_REQUEST = new DiaryRequestDto("diary1", true);

    public static final MockMultipartFile MOCK_IMAGE_FILE = new MockMultipartFile(
            "다이어리 썸네일 이미지",
            "thumbnail.png",
            MediaType.IMAGE_PNG_VALUE,
            "thumbnail".getBytes()
    );

}
