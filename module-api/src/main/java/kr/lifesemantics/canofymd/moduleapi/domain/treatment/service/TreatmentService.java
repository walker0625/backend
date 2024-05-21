package kr.lifesemantics.canofymd.moduleapi.domain.treatment.service;

import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.service.BpHistoryService;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.vo.BpSummary;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.vo.PeriodVO;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientBasicInfo;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.service.PatientService;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.dto.req.TreatmentModifyReq;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.dto.req.TreatmentRegisterReq;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.dto.res.TreatmentFindListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.dto.res.TreatmentFindRes;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.mapper.TreatmentMapper;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.repository.TreatmentRepository;
import kr.lifesemantics.canofymd.moduleapi.global.util.StatisticsUtil;
import kr.lifesemantics.canofymd.modulecore.domain.bp.BpHistory;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.domain.bp.Treatment;
import kr.lifesemantics.canofymd.modulecore.exception.BusinessException;
import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.treatment.service
 * fileName       : TreatmentService
 * author         : ms.jo
 * date           : 2024/04/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/26        ms.jo       최초 생성
 */
@Service
@Slf4j
@Transactional
public class TreatmentService {

    TreatmentRepository treatmentRepository;
    PatientService patientService;
    TreatmentMapper treatmentMapper;
    BpHistoryService bpHistoryService;

    // TODO 순환참조 발생(BpHistoryService <-> TreatmentService)으로 인하여 @Lazy 로딩(추후 설계변경(파사드 패턴 등)으로 해결 지향)
    public TreatmentService(@Lazy BpHistoryService bpHistoryService, TreatmentRepository treatmentRepository, PatientService patientService, TreatmentMapper treatmentMapper) {
        this.bpHistoryService = bpHistoryService;
        this.treatmentRepository = treatmentRepository;
        this.patientService = patientService;
        this.treatmentMapper = treatmentMapper;
    }

    @Transactional
    public TreatmentFindRes create(Long hospitalSeq, TreatmentRegisterReq request) {

        Patient patient = patientService.findById(request.getPatientSeq());
        PatientBasicInfo basicInfo = patientService.getBasicInfo(request.getPatientSeq());

        Treatment treatment = treatmentRepository.save(
                Treatment.create(
                        patient,
                        request.isHasCAD(), request.isHasPAOD(), request.isHasCVA(), request.isHasCKD(), request.isHasHF(),
                        request.isHasAneurysm(), request.isHasHypertension(), request.isHasHL(), request.isHasDM(),
                        request.isHasMeasurement(), request.getMeasuredDate(), request.getSystolic(), request.getDiastolic(),
                        request.isHasBloodCheck(), request.getBloodCheckDate(), request.getTChol(), request.getTg(), request.getHdl(), request.getLdl(), request.getFbs(),
                        request.isHasUrineTest(), request.getUrineTestDate(), request.getUProt(), request.getUAlbDCr(),
                        request.isHasElectrocardiogramTest(), request.getElectrocardiogramTestDate(), request.getLvh(), request.getTreatedAt()
                )
        );

        //TODO change put on BPSummary:List
        //statisticsService.synchronizeTreatment(treatment, null);

        PeriodVO periodVO = getTreatmentSeparateFromDate(request.getPatientSeq()).entrySet()
                .stream()
                .filter(i -> i.getKey().isContains(treatment.getTreatedAt()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ResponseStatus.FAILED_REGISTER_TREATMENT, HttpStatus.BAD_REQUEST));

        System.out.println("periodVO = " + periodVO);

        List<BpHistory> histories = bpHistoryService.getByPatientSeqAndIsUsableIsTrueAndMeasureTimeBetween(
                request.getPatientSeq(),
                LocalDateTime.of(periodVO.from(), LocalTime.of(0, 0, 0)),
                LocalDateTime.of(periodVO.to(), LocalTime.of(23, 59, 59))
        );
        List<BpSummary> summaries = bpHistoryService.calculateAverage(histories);

        treatment.changeCardiovascularDiseaseRisk(StatisticsUtil.getCardiovascularDisease(treatment, summaries));
        treatment.changeControlItems(StatisticsUtil.getBPControl(treatment, summaries), StatisticsUtil.getMedicationControl(treatment, summaries));
        treatment.changeGoalLDL(StatisticsUtil.getGoalLDL(treatment, summaries));

        return treatmentMapper.toTreatmentFindRes(basicInfo, treatment);
    }

    @Transactional(readOnly = true)
    public Treatment findById(Long treatmentSeq) {
        return treatmentRepository.findById(treatmentSeq).orElseThrow(() -> new BusinessException(ResponseStatus.NOT_EXIST_TREATMENT, HttpStatus.BAD_REQUEST));
    }

    @Transactional(readOnly = true)
    public List<TreatmentFindListRes> findTreatmentFindResListByPatient(Long patientSeq) {
        List<Treatment> treatments = getByPatientSeqOrderByTreatedAtDesc(patientSeq);

        return treatments.stream().map(treatment -> treatmentMapper.toTreatmentFindListRes(treatment)).toList();
    }

    @Transactional(readOnly = true)
    public List<Treatment> getByPatientSeqOrderByTreatedAtDesc(Long patientSeq) {

        List<Treatment> result = treatmentRepository.findByPatientSeqOrderByTreatedAtDesc(patientSeq);

        if(result.isEmpty()) throw new BusinessException(ResponseStatus.NOT_EXIST_TREATMENT, HttpStatus.NOT_FOUND);

        return result;
    }

    @Transactional(readOnly = true)
    public List<Treatment> getByPatientSeqOrderByTreatedAtAsc(Long patientSeq) {

        List<Treatment> result = treatmentRepository.findByPatientSeqOrderByTreatedAtAsc(patientSeq);

        if(result.isEmpty()) throw new BusinessException(ResponseStatus.NOT_EXIST_TREATMENT, HttpStatus.NOT_FOUND);

        return result;
    }

    @Transactional(readOnly = true)
    public TreatmentFindRes getTreatmentDetail(Long treatmentSeq) {

        Treatment treatment = findById(treatmentSeq);
        PatientBasicInfo basicInfo = patientService.getBasicInfo(treatment.getPatient().getSeq());

        return treatmentMapper.toTreatmentFindRes(basicInfo, treatment);

    }

    @Transactional(readOnly = true)
    public TreatmentAnalyticsRes getAnalyticsTreatment(Long patientSeq) {
        Treatment treatment = treatmentRepository.findFirstByPatientSeqOrderByTreatedAtDesc(patientSeq).orElse(Treatment.createEmptyObject());
        return treatmentMapper.toTreatmentAnalyticsRes(treatment);
    }

    @Transactional
    public TreatmentFindRes modifyTreatment(Long treatmentSeq, TreatmentModifyReq request) {

        Treatment treatment = findById(treatmentSeq);
        Patient patient = treatment.getPatient();
        PatientBasicInfo basicInfo = patientService.getBasicInfo(patient.getSeq());

        treatment.modify(
                request.isHasCAD(), request.isHasPAOD(), request.isHasCVA(), request.isHasCKD(), request.isHasHF(),
                request.isHasAneurysm(), request.isHasHypertension(), request.isHasHL(), request.isHasDM(),
                request.isHasMeasurement(), request.getMeasuredDate(), request.getSystolic(), request.getDiastolic(),
                request.isHasBloodCheck(), request.getBloodCheckDate(), request.getTChol(), request.getTg(), request.getHdl(), request.getLdl(), request.getFbs(),
                request.isHasUrineTest(), request.getUrineTestDate(), request.getUProt(), request.getUAlbDCr(),
                request.isHasElectrocardiogramTest(), request.getElectrocardiogramTestDate(), request.isLvh(), request.getTreatedAt()
        );

        treatment = findById(treatmentSeq);

        //TODO change put on BPSummary:List
        //statisticsService.synchronizeTreatment(treatment, null);

        return treatmentMapper.toTreatmentFindRes(basicInfo, treatment);

    }

    @Transactional
    public void deleteTreatment(Long treatmentSeq) {

        Treatment treatment = findById(treatmentSeq);
        treatmentRepository.delete(treatment);

        //TODO change put on BPSummary:List
        //statisticsService.synchronizeTreatment(treatment, null);

    }

    @Transactional(readOnly = true)
    public Map<PeriodVO, Treatment> getTreatmentSeparateFromDate(Long patientSeq) {
            List<Treatment> treatments = getByPatientSeqOrderByTreatedAtAsc(patientSeq);

            Map<PeriodVO, Treatment> treatmentMap = new TreeMap<>(Comparator.comparing(PeriodVO::from));

            /**
             * Treatment
             * ---
             * Treatment 1 : treatedAt = 2024.01.01
             * Treatment 2 : treatedAt = 2024.01.08
             * Treatment 3 : treatedAt = 2024.01.15
             *
             *
             * Period
             * Week 1 : measuredDate =
             *
             */
            for (int i = 0; i <= treatments.size(); i++) {

                LocalDate from = i == 0 ? LocalDate.MIN : treatments.get(i-1).getTreatedAt().plusDays(1);
                LocalDate to = i == treatments.size() ? LocalDate.MAX : treatments.get(i).getTreatedAt();

                Treatment treatment = i == 0 ? null : treatments.get(i - 1);

                treatmentMap.put(new PeriodVO(from, to), treatment);

            }

            return treatmentMap;
        }
    }
