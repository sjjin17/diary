package todaktodak.domain.auth.dto.response;

import lombok.Builder;

@Builder
public record TokenResponseDto(String accessToken) {
}
