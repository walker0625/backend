package kr.lifesemantics.canofymd.modulecore.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.lifesemantics.canofymd.modulecore.exception.BusinessException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ResponseStatus {

	SUCCESS(1000, "Success"),

	/**
	 * 1xxx : auth exception
	 */
	INVALID_INPUT(1201, "Invalid Input"),
	EXCEPTION(1999, "Unknown Exception"),
	INVALID_OTP(1202, "Invalid OTP - Over 3 Minute or Wrong OTP"),
	INVALID_REQUEST_OF_TYPE(1203, "Invalid type of request"),



	/**
	 * 2xxx : center exception
	 */
	NOT_EXIST_HOSPITAL(2001, "Fail to find Hospital"),
	NOT_MATCH_CENTER(2002, "Account Hospital and Input Hospital Not Matched"),
	FAILED_REGIST_HOSPITAL(2003, "Failed regist HOSPITAL"),
	JUST_ADMIN_CAN_CREATE_HOSPITAL(2004, "just admin can create HOSPITAL"),


	/**
	 * 3xxx : staff exception
	 */
	FAILED_REGISTER_ADMIN(3001, "fail to register admin"),
	FAILED_UPLOAD_HOSPITAL_LOGO_ON_FILE_SERVER(3002, "failed upload hospital logo image on file server."),
	FAILED_MODIFY_HOSPITAL_INFO(3003, "failed modify hospital basic info"),
	FAILED_MODIFY_STAFF_INFO(3004, "failed modify user basic info"),
	FAILED_MODIFY_STAFF_PROFILE_ON_FILE_SERVER(3005, "failed upload user profile image on file server."),
	FAILED_SAVE_PATIENT_LABORATORY(3006, "failed to save patient laboratory."),
	PASSWORD_DOES_NOT_MATCH(3007, "Please enter the password correctly"),
	FAILED_MODIFY_PASSWORD(3008, "Failed to modify password."),
	FAILED_MODIFY_DOCTOR_DETAIL(3009, "Failed to modify doctor detail info."),
	FAILED_MODIFY_DOCTOR_TIMETABLE(3010, "Failed to modify doctor time table."),
	INVALID_PASSWORD(3011, "Invalid password."),
	FAILED_REGISTER_STAFF(3012, "fail to register STAFF"),
	SAME_PASSWORD(3013, "Please enter the new password."),
	NOT_EXIST_STAFF(3014, "Fail to find user"),



	/**
	 * 4xxx : patient exception
	 */
	FAILED_REGISTER_PATIENT(4001, "fail to register subject"),
	NOT_EXIST_PATIENT(4002, "Fail to find subject"),
	FAILED_MODIFY_PATIENT(4003, "Fail to modify subject"),
	FAILED_MATCH_PASSWORD(4004, "check password"),
	CAN_NOT_FIND_PUSH_TOKEN(4005, "can not find push token"),



	/**
	 * 5xxx : bp exception
	 */
	NOT_EXIST_BP(5001, "Fail to find BP"),
	NOT_EXIST_SUMMARY(5002, "Not exist summary"),

	CAN_NOT_PREDICT(5003, "Can not prediction"),


	/**
	 * 6xxx : treatment exception
	 */
	FAILED_REGISTER_TREATMENT(6001, "fail to register treatment"),
	NOT_EXIST_TREATMENT(6002, "Fail to find treatment"),
	HOSPITAL_IS_INACTIVE(6003, "Because of HOSPITAL is inactive, can not reserve them"),

	/**
	 * 7xxx :  sc exception
	 */
	FAILED_DIAGNOSE_FROM_MODEL(7001, "Fail to get sc diagnose from ai model"),
	FAILED_DIAGNOSE_IMAGE_SAVE(7002, "Fail to save original image"),
	FAILED_DIAGNOSE_THUMBNAIL_IMAGE_SAVE(7003, "Fail to save thumbnail image"),
	REQUIRE_ONLY_CSV(7004, "This file is invalid."),
	NOT_EXIST_EVALUATION(7005, "Fail to find evaluation"),
	NO_HISTORY_THIS_EVALUATION(7006, "This item is not in the results of this evaluation"),
	CAN_NOT_PRESENT_TO_GRAPH(7007, "This item can not present to graph"),
	NOT_EXIST_DIAGNOSE(7008, "Fail to find diagnose"),

	/**
	 * 8xxx :  training exception
	 */
	THIS_TYPE_OF_GOTTEN_TRAINING_IS_NOT_EXIST(8001, "This type of gotten training is not exist"),

	/**
	 * 9xxx : global exception type definition
	 */
	CHECK_DATE_FORMAT(9001, "Format of LocalDateTime Type is 'YYYY-mm-ddTHH:MM:ss'. (please check 'T' between Date and Time)"),
	OCCURRED_ON_PARSING(9002, "This problem is occurred to parsing to json"),
	FAILED_SEND_SMS(9003, "Fail Send AppStore Link Sms"),
	FAILED_FILE_FIND(9004, "Fail to Find File")
	;

	private final int code;
	private final String message;
	private Map<String, Object> body;

	private ResponseStatus(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public ResponseStatus body(Map body) {
		this.body = body;
		return this;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		return map;
	}

	public Map<String, Object> toMap(int count) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		map.put("count", count);
		return map;
	}

	public Map<String, Object> toMap(String comment) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		map.put("comment", comment);
		return map;
	}

	public Map<String, Object> toMap(int count, String comment) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		map.put("count", count);
		map.put("comment", comment);
		return map;
	}

	public Map<String, Object> toModelMap(Map<String, Object> modelMap) {
		Map<String, Object> remap = new LinkedHashMap<String, Object>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		remap.put("responseStatus", map);
		for(Entry<String, Object> entry : modelMap.entrySet()) {
			remap.put(entry.getKey(), entry.getValue());
		}
		return remap;
	}

	public Map<String, Map<String, Object>> toRemap() {
		Map<String, Map<String, Object>> remap = new HashMap<String, Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		remap.put("responseStatus", map);
		return remap;
	}

	public Map<String, Map<String, Object>> toRemap(ResponseStatus additionalStatus) {
		Map<String, Map<String, Object>> remap = new HashMap<String, Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("addtion", additionalStatus.getCode());
		map.put("message", additionalStatus.getMessage());
		remap.put("responseStatus", map);
		return remap;
	}

	public Map<String, Map<String, Object>> toRemap2(ResponseStatus additionalStatus) {
		Map<String, Map<String, Object>> remap = new HashMap<String, Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("addtion", additionalStatus.getCode());
		remap.put("responseStatus", map);
		return remap;
	}

	public Map<String, Map<String, Object>> toRemap(int count) {
		Map<String, Map<String, Object>> remap = new HashMap<String, Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		map.put("count", count);
		remap.put("responseStatus", map);
		return remap;
	}

	public Map<String, Map<String, Object>> toRemap(String comment) {
		Map<String, Map<String, Object>> remap = new HashMap<String, Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		map.put("comment", comment);
		remap.put("responseStatus", map);
		return remap;
	}

	public Map<String, Map<String, Object>> toRemap(int count, String comment) {
		Map<String, Map<String, Object>> remap = new HashMap<String, Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		map.put("count", count);
		map.put("comment", comment);
		remap.put("responseStatus", map);
		return remap;
	}

	public Map<String, Map<String, Object>> toRemap(String key, String value) {
		Map<String, Map<String, Object>> remap = new HashMap<String, Map<String, Object>>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		map.put(key, value);
		remap.put("responseStatus", map);
		return remap;
	}

	public Map<String, Object> toInmap(ResponseStatus additionalStatus, String key, Object value) {
		Map<String, Object> inmap = new LinkedHashMap<String, Object>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("addition", additionalStatus.getCode());
		map.put("message", additionalStatus.getMessage());
		inmap.put("responseStatus", map);
		inmap.put(key, value);
		return inmap;
	}

	public Map<String, Object> toInmap2(ResponseStatus additionalStatus, String key, Object value) {
		Map<String, Object> inmap = new LinkedHashMap<String, Object>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("addition", additionalStatus.getCode());
		inmap.put("responseStatus", map);
		inmap.put(key, value);
		return inmap;
	}

	public Map<String, Object> toInmap(ResponseStatus additionalStatus, String key, String comment, Object value) {
		Map<String, Object> inmap = new LinkedHashMap<String, Object>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("addition", additionalStatus.getCode());
		map.put("message", additionalStatus.getMessage());
		map.put("comment", comment);
		inmap.put("responseStatus", map);
		inmap.put(key, value);
		return inmap;
	}

	public Map<String, Object> toInmap2(ResponseStatus additionalStatus, String key, String comment, Object value) {
		Map<String, Object> inmap = new LinkedHashMap<String, Object>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("addition", additionalStatus.getCode());
		map.put("comment", comment);
		inmap.put("responseStatus", map);
		inmap.put(key, value);
		return inmap;
	}

	public Map<String, Object> toInmap(String key, Object value) {
		Map<String, Object> inmap = new LinkedHashMap<String, Object>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		inmap.put("responseStatus", map);
		inmap.put(key, value);
		return inmap;
	}

	public Map<String, Object> toInmap(String key1, Object value1, String key2, Object value2) {
		Map<String, Object> inmap = new LinkedHashMap<String, Object>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		inmap.put("responseStatus", map);
		inmap.put(key1, value1);
		inmap.put(key2, value2);
		return inmap;
	}

	public Map<String, Object> toInmap(String key, Object meta, Object value) {
		Map<String, Object> inmap = new LinkedHashMap<String, Object>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		inmap.put("responseStatus", map);
		inmap.put("meta", meta);
		inmap.put(key, value);
		return inmap;
	}

	public Map<String, Object> toMessage(String key, Object value) {
		Map<String, Object> inmap = new LinkedHashMap<String, Object>();
		inmap.put("cid", key);
		inmap.put("result", value);
		return inmap;
	}

	public Map<String, Object> toInmap(int count, String key, Object value) {
		Map<String, Object> inmap = new LinkedHashMap<String, Object>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", code);
		map.put("message", message);
		map.put("count", count);
		inmap.put("responseStatus", map);
		inmap.put(key, value);
		return inmap;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"code\":").append(code).append(", ");
		sb.append("\"message\":\"").append(message).append("\"");
		sb.append("}");
		return sb.toString();
	}

	public String toString(Object comment) {

		ObjectMapper objectMapper = new ObjectMapper();

		try {
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			sb.append("\"code\":").append(code).append(", ");
			sb.append("\"message\":\"").append(message).append("\", ");
			sb.append("\"body\":").append(objectMapper.writeValueAsString(body));
			sb.append("}");

			return sb.toString();
		} catch (JsonProcessingException e) {
			throw new BusinessException(INVALID_INPUT, HttpStatus.BAD_REQUEST);
		}

	}

	public static final Map<Integer, String> BY_CODE =
				sortMapByKey(Stream.of(values()).collect(Collectors.toMap(ResponseStatus::getCode, ResponseStatus::getMessage)));

	private static LinkedHashMap<Integer, String> sortMapByKey(Map<Integer, String> map) {
	        List<Entry<Integer, String>> entries = new LinkedList<>(map.entrySet());
	        Collections.sort(entries, Entry.comparingByKey());

	        LinkedHashMap<Integer, String> result = new LinkedHashMap<>();
	        for (Entry<Integer, String> entry : entries) {
	            result.put(entry.getKey(), entry.getValue());
	        }
	        return result;
	}

}