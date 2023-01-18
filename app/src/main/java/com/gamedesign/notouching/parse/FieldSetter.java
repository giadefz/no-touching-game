package com.gamedesign.notouching.parse;

import com.gamedesign.notouching.component.Component;

import java.lang.reflect.Field;

import lombok.SneakyThrows;

@FunctionalInterface
public interface FieldSetter {

    void setField(Field field, String fieldValue, Component object);

}
