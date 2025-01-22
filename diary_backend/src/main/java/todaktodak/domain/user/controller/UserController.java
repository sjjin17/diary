package todaktodak.domain.user.controller;

import todaktodak.domain.user.service.UserService;
import todaktodak.global.api.BasicResponse;
import todaktodak.global.api.CommonResponse;
import todaktodak.global.common.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 일기장 목록 조회
    @GetMapping("/me/diaries")
    public ResponseEntity<? extends BasicResponse> getAllDiaries(@LoginUser Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(userService.getAllDiary(userId)));

    }

}
