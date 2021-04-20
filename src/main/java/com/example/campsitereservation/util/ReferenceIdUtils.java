package com.example.campsitereservation.util;

import java.util.UUID;

public class ReferenceIdUtils {

    public static String generateRefId(int length) {
        String randomUuid = UUID.randomUUID().toString() + UUID.randomUUID().toString();
        return randomUuid.replaceAll("-", "").substring(0, length);
    }
}
