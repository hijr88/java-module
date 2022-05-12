package me.yh.java.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Beans {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean(name = "customRestTemplate")
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(3000);
        restTemplate.setRequestFactory(httpRequestFactory);

        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        for (int i=0; i<messageConverters.size(); i++) {
            HttpMessageConverter<?> messageConverter = messageConverters.get(i);
            if (messageConverter instanceof StringHttpMessageConverter) {
                messageConverters.set(i, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            }

            if (messageConverter instanceof AllEncompassingFormHttpMessageConverter) {

                Class<FormHttpMessageConverter> clazz = FormHttpMessageConverter.class;
                FormHttpMessageConverter formHttpMessageConverter = (FormHttpMessageConverter) messageConverter;

                try {
                    Field f = clazz.getDeclaredField("partConverters");
                    f.setAccessible(true);

                    List<HttpMessageConverter<?>> partConverters  = (List<HttpMessageConverter<?>>) f.get(formHttpMessageConverter);
                    //기존 StringHttpMessageConverter 변경
                    for (int j=0; j<partConverters.size(); j++) {
                        if (partConverters.get(j) instanceof StringHttpMessageConverter) {
                            StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
                            stringHttpMessageConverter.setWriteAcceptCharset(false);
                            partConverters.set(j, stringHttpMessageConverter);
                            break;
                        }
                    }
                } catch (NoSuchFieldException e) {
                    log.error("NoSuchField : partConverters");
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage());
                }

                formHttpMessageConverter.setCharset(StandardCharsets.UTF_8);
                formHttpMessageConverter.setMultipartCharset(StandardCharsets.UTF_8);
            }
        }
        return restTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}
