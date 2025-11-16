package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();
    @Value("${openai.key}")
    private String apikey;
    @Value("${openai.url}")
    private String apiUrl;
    @Value("${openai.model}")
    private String model;
    /**
     * 传入 Java 远吗 -> 返回 ai 生成的 JUnit5 单测代码
     *
     */
    public String generateUnitTest(String javaSource) {
        String prompt = """
              You are a Java expert . Below is a Java class.
              Generate only the JUnit5 unit test code (no explanation,no markdown code block ).
              Import all necessary packages.
              Java class:
              %s
              """.formatted(javaSource);
        //构造 格式请求体
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(Map.of("role","user","content",prompt)),
                "temperature", 0.2 //降低随机性
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apikey);
        HttpEntity<String> entity = new HttpEntity<>(toJson(body),headers);
        try{
            ResponseEntity<Map> resp = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );
            List<Map<String,Object>> choices = (List<Map<String,Object>>)
                    resp.getBody().get("choices");
            Map<String,Object> message = (Map<String,Object>)
                    choices.get(0).get("message");
            return (String) message.get("content");
        }catch (Exception e){
            log.error("Error: {}",e.getMessage());
        }

        return null;
    }

    private String toJson(Object obj0){
        try{
            return mapper.writeValueAsString(obj0);
        }catch (Exception e){
            log.error("Error: {}",e.getMessage());
        }
        return null;
    }




}
