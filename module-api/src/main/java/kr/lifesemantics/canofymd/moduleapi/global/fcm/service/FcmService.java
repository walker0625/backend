package kr.lifesemantics.canofymd.moduleapi.global.fcm.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.service.PatientService;
import kr.lifesemantics.canofymd.moduleapi.global.fcm.dto.FcmSendReq;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.exception.BusinessException;
import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.global.fcm
 * fileName       : FcmService
 * author         : ms.jo
 * date           : 2024/05/02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/02        ms.jo       최초 생성
 */
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FcmService {

	FirebaseMessaging firebaseMessaging;
    ObjectMapper mapper = new ObjectMapper();
	String ALARM_NAME = "테스트 알람 이름";
	String ALARM_CONTENTS = "테스트 알람 컨텐츠";

    	{
    		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    	}

    	/**
    	 *
    	 * @Author : ms.jo
    	 * @param : recordkey(String) : 환자 idx
    	 * 			pushType(PushType) : push Message의 type
    	 * 			params(HashMap<String, String>) : 해당 type의 push를 보내기 위한 params
    	 *
    	 * pushtoken, title, content, scheme
    	 * */
    	public void send(FcmSendReq request) {

    		//인증에 필요한 토큰들 생성부
			String pushToken = request.getPushToken();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(!Objects.isNull(pushToken)) {
				Notification notification = Notification.builder()
						.setTitle(ALARM_NAME)
						.setBody(ALARM_CONTENTS)
						.build();

				Message message = Message.builder()
						.setToken(pushToken)
						.setNotification(notification)
						.build();

				try {
					firebaseMessaging.send(message);
//					log.info("sending message is successful (to :: patient'{}')", request.getPatientSeq());
				} catch (FirebaseMessagingException e) {
//					log.info("sending message is failure (to :: patient'{}')", request.getPatientSeq());
					throw new RuntimeException(e);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
			else {
				throw new BusinessException(ResponseStatus.CAN_NOT_FIND_PUSH_TOKEN, HttpStatus.NO_CONTENT);
			}
    	}

}
