package me.yh.java.api;

import com.fasterxml.jackson.core.type.TypeReference;
import me.yh.java.util.Converter;
import me.yh.java.util.CustomException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RequestApi {

    private final RestTemplate restTemplate;
    private final Converter converter;

    @Autowired
    public RequestApi(RestTemplate restTemplate, Converter converter) {
        this.restTemplate = restTemplate;
        this.converter = converter;
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String HOST = "localhost";

    public String saveImage(Map<String, Object> params) {
        String url = "/images";

        HttpHeaders headers = getHttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        final MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        Map<String, Object> responseMap = post(url, headers, map);
        if (isError(responseMap)) {
            throw new CustomException(String.valueOf(responseMap.get("message")));
        }

        return String.valueOf(responseMap.get("image"));
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        return headers;
    }

    private Map<String, Object> get(String url, HttpHeaders headers, Map<String, String> data) {
        if (data != null && !data.isEmpty()) {
            String queryString = mapToQueryString(data);
            url = url + "?" + queryString;
        }

        return getResponse(url, HttpMethod.GET, headers, data);
    }

    private Map<String, Object> post(String url, HttpHeaders headers, Object data) {
        return getResponse(url, HttpMethod.POST, headers, data);
    }

    private Map<String, Object> put(String url, HttpHeaders headers, Object data) {
        return getResponse(url, HttpMethod.PUT, headers, data);
    }

    private Map<String, Object> delete(String url, HttpHeaders headers, Object data) {
        return getResponse(url, HttpMethod.DELETE, headers, data);
    }

    private Map<String, Object> getResponse(String url, HttpMethod method, HttpHeaders headers, Object data) {

        url = HOST + url;

        HttpEntity<?> entity = new HttpEntity<>(data, headers);

        return callRestTemplate(url, method, entity);
    }

    private Map<String, Object> callRestTemplate(String url, HttpMethod method, HttpEntity<?> entity) {
        String response;

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, entity, String.class);
            response = responseEntity.getBody();


        } catch(HttpStatusCodeException e) {
            response = e.getResponseBodyAsString();
        } catch (Exception e) {
            log.error("RequestApi-callRestTemplate {}", e.getMessage());
            throw new RuntimeException("Connect Fail Center-Api");
        }

        log.debug("응답: " + response);
        return converter.jsonToMap(response, new TypeReference<>() {
        });
    }

    private boolean isError(Map<String, Object> responseMap) {
        return !"200".equals(responseMap.get("code"));
    }

    private String mapToQueryString(Map<String, String> params) {
        List<BasicNameValuePair> nameValuePairs = params.entrySet().stream()
                .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return URLEncodedUtils.format(nameValuePairs, StandardCharsets.UTF_8);
    }
}
