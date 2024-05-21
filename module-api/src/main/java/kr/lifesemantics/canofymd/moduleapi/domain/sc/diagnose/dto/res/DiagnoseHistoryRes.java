package kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res
 * fileName       : DiagnoseHistoryRes
 * author         : ms.jo
 * date           : 2024/05/16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/16        ms.jo       최초 생성
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "이전 감별 내역 리스트 DTO")
@AllArgsConstructor
public class DiagnoseHistoryRes {

    @Schema(description = "감별 순차 번호", example = "6")
    Long diagnoseSeq;
    @Schema(description = "회원 순차 번호", example = "1")
    Long patientSeq;
    @Schema(description = "촬영 부위 이미지 썸네일 url")
    String thumbnailUrl;
    @Schema(description = "음성 확률", example = "65.3")
    double isCancerRate;
    @Schema(description = "양성 확률", example = "34.7")
    double isNotCancerRate;
    @Schema(description = "감별일시", example = "2024-04-17 13:24")
    LocalDateTime diagnosedAt;
    @JsonIgnore
    String filekey;
    @JsonIgnore
    String filename;

    public DiagnoseHistoryRes(Long seq, Long patientSeq, String imageThumbnailUrl, double isCancerRate, double isNotCancerRate, LocalDateTime diagnosedAt) {
        this.diagnoseSeq = seq;
        this.patientSeq = patientSeq;
        this.thumbnailUrl = imageThumbnailUrl;
        this.isCancerRate = isCancerRate;
        this.isNotCancerRate = isNotCancerRate;
        this.diagnosedAt = diagnosedAt;
    }

    @QueryProjection
    public DiagnoseHistoryRes(Long seq, Long patientSeq, double isCancerRate, double isNotCancerRate, LocalDateTime diagnosedAt, String filekey, String filename) {
        this.diagnoseSeq = seq;
        this.patientSeq = patientSeq;
        this.isCancerRate = isCancerRate;
        this.isNotCancerRate = isNotCancerRate;
        this.diagnosedAt = diagnosedAt;
        this.filekey = filekey;
        this.filename = filename;
    }

}