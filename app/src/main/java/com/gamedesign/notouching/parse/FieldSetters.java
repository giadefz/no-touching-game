package com.gamedesign.notouching.parse;

public enum FieldSetters {

    INT((f, v, c) -> {
        try {
            f.setAccessible(true);
            f.setInt(c, Integer.parseInt(v));
        } catch (IllegalAccessException e) {
            throw new ParseGameObjectJSONException("Field " + f.getName() + "is either inaccessible or final");
        }
    }),

    FLOAT((f, v, c) -> {
        try {
            f.setAccessible(true);
            f.setFloat(c, Float.parseFloat(v));
        } catch (IllegalAccessException e) {
            throw new ParseGameObjectJSONException("Field " + f.getName() + "is either inaccessible or final");
        }
    }),

    BOOLEAN((f, v, c) -> {
        try {
            f.setAccessible(true);
            f.setBoolean(c, Boolean.parseBoolean(v));
        } catch (IllegalAccessException e) {
            throw new ParseGameObjectJSONException("Field " + f.getName() + "is either inaccessible or final");
        }
    }),

    ;

    private final FieldSetter fieldSetter;

    FieldSetters(FieldSetter fieldSetter) {
        this.fieldSetter = fieldSetter;
    }

    public FieldSetter getSetter() {
        return fieldSetter;
    }
}
