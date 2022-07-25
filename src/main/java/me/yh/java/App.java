package me.yh.java;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID().toString().replaceAll("-", "").substring(0,25));

    }

}
