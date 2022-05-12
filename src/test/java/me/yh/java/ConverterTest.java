package me.yh.java;

import com.fasterxml.jackson.core.type.TypeReference;
import me.yh.java.util.Converter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class ConverterTest {

    @Autowired
    Converter converter;

    @Test
    void main() {
        List<String> list = converter.jsonToList("[123, 456]", new TypeReference<>() {});
        System.out.println(list);

        Map<String, Object> map = converter.jsonToMap("{\"a\":\"123\",\"b\":\"456\"}", new TypeReference<>() {});
        System.out.println(map);
    }
}
