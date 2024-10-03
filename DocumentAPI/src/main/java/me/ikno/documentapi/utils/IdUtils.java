package me.ikno.documentapi.utils;

public class IdUtils {
    public static String compressUuid(String uuid) {
        return uuid.replace("-", "");
    }
}
