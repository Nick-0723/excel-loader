package com.hjonline.loader;

import lombok.Data;

/**
 * @author nick
 * @date 2018/12/3
 */
@Data
public class Field {
    private String fieldName;
    private String fieldType;

    public Field(String fieldName, String fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }
}
