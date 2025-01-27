package todaktodak.domain.diary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import todaktodak.domain.diary.domain.Diary;
import todaktodak.domain.diary.dto.request.DiaryRequestDto;
import todaktodak.domain.diary.dto.response.DiaryDetailResponseDto;
import todaktodak.domain.diary.repository.DiaryRepository;
import todaktodak.domain.user.domain.Member;
import todaktodak.domain.user.repository.MemberRepository;
import todaktodak.domain.user.repository.UserRepository;
import todaktodak.global.config.s3.S3Config;
import todaktodak.global.config.s3.S3SaveDir;
import todaktodak.global.config.s3.S3Service;
import todaktodak.global.exception.CustomException;
import todaktodak.global.exception.ErrorCode;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
    public DiaryDetailResponseDto createDiary(Long userId, DiaryRequestDto diaryRequestDto, MultipartFile thumbnail) throws IOException {
        String imageName = UUID.randomUUID().toString() + "_" + thumbnail.getOriginalFilename();
        String imageUrl = s3Service.uploadFile(thumbnail, S3SaveDir.DIARY, imageName);
        Diary diary = diaryRequestDto.toEntity(imageUrl);
        diaryRepository.save(diary);
        Member member = Member.builder()
                        .diary(diary)
                        .user(userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS)))
                        .build();

        memberRepository.save(member);
        diary.addMember(member);
        return DiaryDetailResponseDto.from(diary);

    }


}
