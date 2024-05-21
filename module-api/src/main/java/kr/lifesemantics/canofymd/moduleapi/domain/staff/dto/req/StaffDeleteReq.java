package kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.req
 * fileName       : StaffDeleteReq
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
@Schema(title = "병원 관계자 삭제 요청 DTO")
@AllArgsConstructor
public class StaffDeleteReq {

    List<Long> seqList;

}
