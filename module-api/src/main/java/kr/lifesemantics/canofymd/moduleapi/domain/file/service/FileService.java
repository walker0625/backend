package kr.lifesemantics.canofymd.moduleapi.domain.file.service;

import kr.lifesemantics.canofymd.modulecore.exception.BusinessException;
import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    @Value("${scai.file.root}")
    private String SCAI_FILE_ROOT;

    public byte[] skinCancerImage(Long patientSeq, String imageType, String imageKey) {

        String imagePath = SCAI_FILE_ROOT + patientSeq + "/" + imageType + "/" + imageKey;

        try {
            return Files.readAllBytes(new File(imagePath).toPath());
        } catch (IOException e) {
            log.error("file find error {}", e.toString());
            throw new BusinessException(ResponseStatus.FAILED_FILE_FIND, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
