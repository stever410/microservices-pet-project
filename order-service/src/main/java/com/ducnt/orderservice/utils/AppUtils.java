package com.ducnt.orderservice.utils;

import java.util.Date;
import java.util.List;

public class AppUtils {

    public static String generateOrderNumber() {
        try {
            String now = Long.toString(new Date().getTime());
            if (now.length() < 14) {
                int pad = 14 - now.length();
                now += generateSalt(pad);
            }
            return String.join("-", List.of(now.substring(0, 4), now.substring(4, 10), now.substring(10, 14)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateSalt(int length) {
        return Double.toString(Math.floor(Math.pow(10, length - 1) +
                Math.random() * (Math.pow(10, length) - Math.pow(10, length - 1) - 1)
        ));
    }
}
