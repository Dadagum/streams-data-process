package com.smartgreen.utils;

import java.util.UUID;

public class UUIDUtils {

    public synchronized static String get() {
        return UUID.randomUUID().toString().toLowerCase();
    }

}
