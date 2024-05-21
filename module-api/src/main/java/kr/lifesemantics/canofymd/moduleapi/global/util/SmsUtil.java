package kr.lifesemantics.canofymd.moduleapi.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class SmsUtil {

    private static final String SMS_HOST = "http://rest.supersms.co:6200/sms/xml";
    private static final String SMS_ID = "lifesemantics";
    private static final String SMS_PASSWORD = "6415UBRI00ORL9096NAC";
    private static final String SMS_FROM = "16612858";
    private static final String SMS_COUNTRY_CODE = "82";

    public static void sendSmsForAppStoreLink(String phoneNumber) throws IOException {

        // TODO 추후 앱스토어 배포되면 해당 설치 링크로 교체
        String appStoreLink = "test";

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(SMS_HOST).append("?");
        urlBuilder.append("id=").append(URLEncoder.encode(SMS_ID, StandardCharsets.UTF_8));
        urlBuilder.append("&pwd=").append(URLEncoder.encode(SMS_PASSWORD, StandardCharsets.UTF_8));
        urlBuilder.append("&from=").append(SMS_FROM);
        urlBuilder.append("&to_country=").append(SMS_COUNTRY_CODE);
        urlBuilder.append("&to=").append(phoneNumber);
        urlBuilder.append("&message=").append(URLEncoder.encode(appStoreLink, StandardCharsets.UTF_8));
        urlBuilder.append("&report_req=1"); //레포트 수신여부 '1' = 레포트, 수신 필수 셋팅

        URL url = new URL(new String(urlBuilder));
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        int responseCode = httpURLConnection.getResponseCode();

        httpURLConnection.disconnect();
    }

}
