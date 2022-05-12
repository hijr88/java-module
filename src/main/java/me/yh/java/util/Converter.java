package me.yh.java.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class Converter {

    private final ObjectMapper mapper;

    public <T> List<T> jsonToList(String str, TypeReference<List<T>> type) {
        try {
            return mapper.readValue(str, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <K, V> Map<K, V> jsonToMap(String str, TypeReference<Map<K, V>> type) {
        try {
            return mapper.readValue(str, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T jsonToClass(String str, TypeReference<T> type) {
        try {
            return mapper.readValue(str, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T objToClass(Object obj, Class<T> cls) {
        return mapper.convertValue(obj, cls);
    }

    public <K, V> Map<K, V> objToMap(Object obj, TypeReference<Map<K, V>> type) {
        return mapper.convertValue(obj, type);
    }
}