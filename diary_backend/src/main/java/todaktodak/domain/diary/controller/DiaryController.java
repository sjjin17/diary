package todaktodak.domain.diary.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import todaktodak.domain.diary.dto.request.DiaryRequestDto;
import todaktodak.domain.diary.dto.request.UpdateDiaryRequestDto;
import todaktodak.domain.diary.service.DiaryService;
import todaktodak.global.api.BasicResponse;
import todaktodak.global.api.CommonResponse;
import todaktodak.global.common.LoginUser;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<? extends BasicResponse> createDiary(@LoginUser Long userId, @RequestPart(value="diaryRequestDto") DiaryRequestDto diaryRequestDto, @RequestPart(value="image") MultipartFile thumbnail) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(diaryService.createDiary(userId, diaryRequestDto, thumbnail)));
    }

    @PutMapping("/{diaryId}")
    public ResponseEntity<? extends BasicResponse> updateDiary(@LoginUser Long userId, @PathVariable Long diaryId, @RequestPart(value="updateDiaryRequestDto") UpdateDiaryRequestDto updateDiaryRequestDto, @RequestPart(value="image") MultipartFile thumbnail) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommonResponse<>(diaryService.updateDiary(userId, diaryId, updateDiaryRequestDto, thumbnail)));
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<? extends BasicResponse> exitDiary(@LoginUser Long userId, @PathVariable Long diaryId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(diaryService.exitDiary(userId, diaryId)));
    }




}
