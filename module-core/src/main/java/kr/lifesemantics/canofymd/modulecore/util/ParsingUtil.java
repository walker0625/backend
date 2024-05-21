package kr.lifesemantics.canofymd.modulecore.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import kr.lifesemantics.canofymd.modulecore.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * packageName    : kr.lifesemantics.canofymd.web.global.util
 * fileName       : ParsingUtil
 * author         : ms.jo
 * date           : 2024/03/27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/03/27        ms.jo       최초 생성
 */

@Slf4j
public final class ParsingUtil {

    private static ParsingUtil parsingUtil = new ParsingUtil();

    private static ObjectMapper objectMapper;

    private ParsingUtil() {
        this.objectMapper = new ObjectMapper();
    }


    public static Map parsing(String data) throws JsonProcessingException {
        Map<String, String> map = objectMapper.readValue(data, Map.class);
        Map<String, Double> result;

        try {
            result = map.entrySet().stream().collect(
                Collectors.toMap(
                        key -> key.getKey(),
                        value -> Double.parseDouble(String.valueOf(value.getValue()))));

        } catch (Exception e) {
            throw new BusinessException(ResponseStatus.CAN_NOT_PRESENT_TO_GRAPH, HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    public static Map<String, Object> parsingCSV(MultipartFile multipartFile) {

            if(multipartFile == null) {
                return Map.of();
            }

            List<Map<String, Object>> maps = Lists.newArrayList();
            List<String> headers = Lists.newArrayList();

            String fileOriginName = multipartFile.getOriginalFilename();

            long count = fileOriginName.length() - fileOriginName.replace(".", "").length();

            if(count != 1 || (!fileOriginName.endsWith(".csv") && !fileOriginName.endsWith(".xlsx"))) {
                throw new BusinessException(ResponseStatus.REQUIRE_ONLY_CSV.body(Map.of("file", fileOriginName)), HttpStatus.BAD_REQUEST);
            }

            try {
                File file = new File(fileOriginName);
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(multipartFile.getBytes());
                fos.close();

                log.info("file.length() :: {}", file.length());

                BufferedReader br = new BufferedReader(new FileReader(file));
                String lines = "";

                while ((lines = br.readLine()) != null) {
                    List<String> stringList = Lists.newArrayList(lines.split(","));

                    if(headers.isEmpty()) {
                        headers = stringList;
                    } else {
                        Map<String, Object> map = Maps.newHashMap();

                        for (int i = 0; i < headers.size(); i++) {

                            String header = headers.get(i);
                            String after = header.substring(1, header.length() - 1);

                            map.put(after, stringList.get(i));
                        }

                        maps.add(map);

                    }

                }
                file.delete();

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return maps.get(0);
        }


}
