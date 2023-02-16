package com.gamedesign.notouching.util;

import com.google.gson.Gson;

import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class GsonMapper {

    private static final Gson gson = new Gson();


    public static <T> T fromJson(Reader reader, Class<T> objectClass) {
        return gson.fromJson(reader, objectClass);
    }

    public static String toJsonString(Object src) {
        return gson.toJson(src);
    }

    public static byte[] toJsonByteArray(Object src) {
        return gson.toJson(src).getBytes(StandardCharsets.UTF_8);
    }
}
