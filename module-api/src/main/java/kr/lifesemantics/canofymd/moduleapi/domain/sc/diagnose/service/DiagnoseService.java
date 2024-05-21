package kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.service;

import kr.lifesemantics.canofymd.moduleapi.domain.patient.controller.PatientFindByDoctorListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientBasicInfo;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.service.PatientService;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.req.DiagnoseAiReq;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.req.DiagnoseResultReq;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res.*;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.mapper.DiagnoseMapper;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.repository.DiagnoseRepository;
import kr.lifesemantics.canofymd.moduleapi.webClient.WebClientService;
import kr.lifesemantics.canofymd.modulecore.domain.sc.Diagnose;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import kr.lifesemantics.canofymd.modulecore.exception.BusinessException;
import kr.lifesemantics.canofymd.modulecore.util.FileUtil;
import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiagnoseService {

    @Value("${scai.file.root}")
    private String SCAI_FILE_ROOT;

    @Value("${scai.image.url.root}")
    private String SCAI_IMAGE_URL_ROOT;

    final PatientService patientService;
    final WebClientService webClientService;

    final DiagnoseRepository diagnoseRepository;
    final DiagnoseMapper diagnoseMapper;

    @Transactional
    public DiagnoseResultRes diagnose(DiagnoseResultReq aiReq) {

        Mono<DiagnoseAiRes> diagnose = webClientService.diagnose(new DiagnoseAiReq(aiReq.getImage(), aiReq.getSpot().getLower()));

        DiagnoseAiRes aiRes = Optional.of(diagnose.block())
                .orElseThrow(() -> new BusinessException(ResponseStatus.FAILED_DIAGNOSE_FROM_MODEL, HttpStatus.INTERNAL_SERVER_ERROR));

        DiagnoseEntity aiResEntity = aiRes.getEntity();

        // 촬영 오류 시 응답
        if (!aiResEntity.isPredictionConfidence()) {
            return DiagnoseResultRes.predictionConfidenceRes(false);
        }

        List<Double> probability = aiResEntity.getProbability();

        BigDecimal bdCancer = new BigDecimal(probability.get(0)).setScale(3, RoundingMode.HALF_UP);
        BigDecimal bd100Cancer = bdCancer.multiply(BigDecimal.valueOf(100));
        BigDecimal bd100NonCancer = BigDecimal.valueOf(100).subtract(bd100Cancer);

        double cancerRate = bd100Cancer.doubleValue();
        double nonCancerRate = bd100NonCancer.doubleValue();

        MultipartFile image = aiReq.getImage();
        String imageName = image.getOriginalFilename();
        String imageKey = UUID.randomUUID().toString();
        Long patientSeq = aiReq.getPatientSeq();

        // thumbnail > original 파일 순으로 저장해야 함
        // original을 먼저 저장(transferTo())하면 thumbnail 만들때 original 파일을 못 찾음
        FileUtil.saveThumbnail(image, SCAI_FILE_ROOT + patientSeq + "/thumbnail/" + imageKey);
        FileUtil.saveOriginal(image, SCAI_FILE_ROOT + patientSeq + "/original/" + imageKey);

        Patient patient = patientService.findById(patientSeq);

        Diagnose latestDiagnose = diagnoseRepository.findTopByOrderByGroupSeqDesc();

        // 첫 저장의 경우 groupSeq == 1
        Long groupSeq = latestDiagnose == null ? 1 : latestDiagnose.getGroupSeq() + 1;

        // 동일 병변 진단 케이스
        if (aiReq.isSameGroup()) {
            Diagnose findedDiagnose = diagnoseRepository.findById(aiReq.getDiagnoseSeq())
                    .orElseThrow(() -> new BusinessException(ResponseStatus.NOT_EXIST_DIAGNOSE, HttpStatus.NOT_FOUND));

            groupSeq = findedDiagnose.getGroupSeq();
        }

        Diagnose savedDiagnose = diagnoseRepository.save(
                Diagnose.create(groupSeq, aiResEntity.getClassName().getAbbr(), aiResEntity.getClassName().getEn(), aiResEntity.getClassName().getKo(),
                        cancerRate, nonCancerRate, aiReq.getComment(), aiReq.getSpot(), imageKey, imageName, patient)
        );

        return DiagnoseResultRes.successRes(savedDiagnose.getSeq());
    }

    @Transactional(readOnly = true)
    public Page<DiagnosePatientRes> diagnoseList(Long staffSeq, Pageable pageable, String keyword) {

        List<Long> patientSeqList = patientService.getPatientsFromDoctor(Category.SCAI, staffSeq, keyword, pageable).map(PatientFindByDoctorListRes::getSeq).toList();
        Map<Long, PatientBasicInfo> basicInfoMap = patientService.getBasicInfos(patientSeqList)
                                                                        .stream()
                                                                        .collect(
                                                                                Collectors.toMap(PatientBasicInfo::getSeq,
                                                                                Function.identity())
                                                                        );

        Page<DiagnoseHistoryRes> histories = diagnoseRepository.findAllByPatientSeqInAndPatientNameContaining(patientSeqList, keyword, pageable).map(i -> {

            String thumbnailUrl = SCAI_IMAGE_URL_ROOT + i.getPatient().getSeq() + "/thumbnail/" + i.getImageKey();

            return new DiagnoseHistoryRes(i.getSeq(), i.getPatient().getSeq(), thumbnailUrl, i.getCancerRate(), i.getNonCancerRate(), i.getCreatedAt());
        });

        return histories.map(i -> new DiagnosePatientRes(basicInfoMap.get(i.getPatientSeq()), i));
    }

    @Transactional(readOnly = true)
    public DiagnoseListForPatientRes getDiagnosesForPatient(Long patientSeq) {

        PatientBasicInfo basicInfo = patientService.getBasicInfo(patientSeq);
        List<Diagnose> diagnoses = diagnoseRepository.findAllByPatientSeqOrderByCreatedAtDesc(patientSeq);
        List<DiagnoseHistoryRes> histories = diagnoses.stream().map(i -> {

            String thumbnailUrl = SCAI_IMAGE_URL_ROOT + i.getPatient().getSeq() + "/thumbnail/" + i.getImageKey();

            return new DiagnoseHistoryRes(i.getSeq(), i.getPatient().getSeq(), thumbnailUrl, i.getCancerRate(), i.getNonCancerRate(), i.getCreatedAt());

        }).toList();

        return diagnoseMapper.toDiagnoseListForPatientRes(basicInfo, histories);
    }

    @Transactional(readOnly = true)
    public DiagnoseDetailRes diagnoseDetail(Long diagnoseSeq) {

        Diagnose diagnose = diagnoseRepository.findById(diagnoseSeq)
                .orElseThrow(() -> new BusinessException(ResponseStatus.NOT_EXIST_DIAGNOSE, HttpStatus.NOT_FOUND));;

        Long patientSeq = diagnose.getPatient().getSeq();

        Patient patient = patientService.findById(patientSeq);

        List<Diagnose> threeDiagnoseList = diagnoseRepository.findTop3ByPatientSeqOrderByCreatedAtDesc(patientSeq);

        List<Diagnose> previousDiagnoseList = threeDiagnoseList.stream()
                .filter(d -> !d.getSeq().equals(diagnoseSeq))
                .limit(2)
                .toList();

        String mainImageUrl = SCAI_IMAGE_URL_ROOT + patientSeq + "/original/" + diagnose.getImageKey();

        List<DiagnoseImage> previousImageList = previousDiagnoseList.stream()
                .map(pd -> diagnoseMapper.toDiagnoseImage(
                    pd.getSeq(),
                    SCAI_IMAGE_URL_ROOT + patientSeq + "/thumbnail/" + pd.getImageKey(),
                    pd.getCancerRate(),
                    pd.getNonCancerRate(),
                    pd.getCreatedAt())
                ).toList();

        return DiagnoseDetailRes.res(patient.getName(), patient.getBirthDate(), patient.getGender(), patient.getContact(),
                diagnose.getSeq(), diagnose.getSpot(), diagnose.getComment(), diagnose.getCancerRate(), diagnose.getNonCancerRate(),
                diagnose.getCancerAbbr(), diagnose.getCancerEn(), diagnose.getCancerKo(), diagnose.getCreatedAt(),
                mainImageUrl, previousImageList);
    }

    @Transactional
    public void diagnoseCommentModify(Long diagnoseSeq, String comment) {

        Diagnose diagnose = diagnoseRepository.findById(diagnoseSeq)
                .orElseThrow(() -> new BusinessException(ResponseStatus.NOT_EXIST_DIAGNOSE, HttpStatus.NOT_FOUND));

        diagnose.modifyComment(comment);
    }

}
