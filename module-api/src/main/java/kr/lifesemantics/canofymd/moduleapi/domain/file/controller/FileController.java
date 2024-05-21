package kr.lifesemantics.canofymd.moduleapi.domain.file.controller;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.lifesemantics.canofymd.moduleapi.domain.file.service.FileService;
import kr.lifesemantics.canofymd.moduleapi.global.config.security.SecurityUserDetails;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme( name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "Bearer")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/files")
@Tag(name = "파일 정보 API", description = "파일 관리 목적의 API Docs")
public class FileController {

    FileService fileService;

    // imageType = [original, thumbnail]
    @GetMapping("/scai/images/{patientSeq}/{imageType}/{imageKey}")
    public ResponseEntity skinCancerImage(@PathVariable Long patientSeq,
                                          @PathVariable String imageType, @PathVariable String imageKey) {

        return ResponseEntity.ok().body(fileService.skinCancerImage(patientSeq, imageType, imageKey));
    }

}
