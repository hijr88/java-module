package me.yh.java.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class Converter {

    ObjectMapper mapper = new ObjectMapper();

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

    public static void main(String[] args) {
        Converter converter = new Converter();

        List<String> list = converter.jsonToList("[123, 456]", new TypeReference<>() {});
        System.out.println(list);

        Map<String, Object> map = converter.jsonToMap("{\"a\":\"123\",\"b\":\"456\"}", new TypeReference<>() {});
        System.out.println(map);
    }
}