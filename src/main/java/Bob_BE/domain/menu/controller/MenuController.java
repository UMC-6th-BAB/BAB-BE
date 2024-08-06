package Bob_BE.domain.menu.controller;

import Bob_BE.domain.menu.dto.request.MenuRequestDto.MenuDeleteRequestDto;
import Bob_BE.domain.menu.dto.request.MenuRequestDto.MenuUpdateRequestDto;
import Bob_BE.domain.menu.dto.response.ImageUploadResponseDto;
import Bob_BE.domain.menu.dto.response.MenuResponseDto;
import Bob_BE.domain.menu.dto.response.MenuResponseDto.DeleteMenuResponseDto;
import Bob_BE.domain.menu.service.MenuService;
import Bob_BE.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PatchMapping("/{menuId}")
    @Operation(summary = "메뉴 업데이트 API", description = "메뉴를 업데이트하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MENU404", description = "해당 메뉴가 존재하지 않습니다.")
    })
    @Parameters({
            @Parameter(name = "menuId", description = "메뉴 식별자, PathVariable")
    })
    public ApiResponse<MenuResponseDto.CreateMenuResponseDto> updateMenu(
            @PathVariable Long menuId,
            @RequestBody @Valid MenuUpdateRequestDto requestDto
    ){
        MenuResponseDto.CreateMenuResponseDto response = menuService.updateMenu(menuId, requestDto);
        return ApiResponse.onSuccess(response);
    }

    @DeleteMapping
    @Operation(summary = "메뉴 삭제 API", description = "여러 메뉴를 삭제하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MENU404", description = "해당 메뉴가 존재하지 않습니다.")
    })
    public ApiResponse<List<DeleteMenuResponseDto>> deleteMenus(
            @RequestBody @Valid MenuDeleteRequestDto requestDto
    ){
        List<DeleteMenuResponseDto> response = menuService.deleteMenus(requestDto);
        return ApiResponse.onSuccess(response);
    }

    @PostMapping(value="/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "메뉴 이미지 업로드 API", description = "메뉴 이미지 파일을 업로드하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S3BUCKET500", description = "파일 업로드에 실패했습니다.")
    })
    public ApiResponse<ImageUploadResponseDto> uploadMenuImage(
            @RequestParam("imageFile") MultipartFile imageFile
    ){
        String imageUrl = menuService.uploadMenuImage(imageFile);
        return ApiResponse.onSuccess(new ImageUploadResponseDto(imageUrl));
    }
}
