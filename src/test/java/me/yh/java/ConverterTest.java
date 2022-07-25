package me.yh.java;

import com.fasterxml.jackson.core.type.TypeReference;
import me.yh.java.util.Converter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ConverterTest {

    @Autowired
    Converter converter;

    @Test
    void main() {
        LocalDate date = LocalDate.parse("20220620", DateTimeFormatter.ofPattern("yyyyMMdd"));
        System.out.println(date);

    }
}
