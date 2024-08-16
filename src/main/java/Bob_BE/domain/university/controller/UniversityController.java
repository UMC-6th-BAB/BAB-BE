package Bob_BE.domain.university.controller;

import Bob_BE.domain.university.converter.UniversityConverter;
import Bob_BE.domain.university.dto.response.UniversityResponseDto;
import Bob_BE.domain.university.entity.University;
import Bob_BE.domain.university.service.UniversityService;
import Bob_BE.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/universities")
public class UniversityController {
    private final UniversityService universityService;

    @GetMapping
    @Operation(summary = "대학교 검색 API",description = "글자 입력 시 해당 글자가 포함된 대학교의 리스트를 반환한다. 글자 입력마다 API가 전송되면 됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "universityName", description = "입력될 대학명"),
    })
    public ApiResponse<List<UniversityResponseDto.UniversitySearchDto>> getUniversitySearchList(@RequestParam String universityName){
        List<University> universityList = universityService.getUniversityByUniversityName(universityName);

        return ApiResponse.onSuccess(UniversityConverter.toUniversitySearchDtoList(universityList));
    }
}
