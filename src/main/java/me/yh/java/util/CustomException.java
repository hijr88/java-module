package me.yh.java.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomException extends RuntimeException {
    private final Logger log = LoggerFactory.getLogger(CustomException.class);

    protected final String message;
    protected final String code;

    public CustomException(String message) {
        this(message, "");
    }

    public CustomException(String message, String code) {
        this.message = message;
        this.code = code;
        print(message, code);
    }

    private void print(String message, String code) {
        if (code.equals("")) {
            log.error("{}: {}", this.getClass().getSimpleName(), message);
        } else {
            log.error("{}: [{}] {}", this.getClass().getSimpleName(), code, message);
        }
    }

    @Override
    public String toString() {
        if (code.equals("")) {
            return  message;
        }
        return "["+ code +"] " + message;
    }
}
