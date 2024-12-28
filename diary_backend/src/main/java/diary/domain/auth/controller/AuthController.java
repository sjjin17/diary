package diary.domain.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import diary.domain.auth.service.AuthService;
import diary.global.api.BasicResponse;
import diary.global.api.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/auth")
public class AuthController {
    private final AuthService authService;


    @GetMapping("/login")
    public ResponseEntity<? extends BasicResponse> login(@RequestParam String code,@RequestParam String provider) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonResponse<>(authService.login(code, provider)));
    }


}
