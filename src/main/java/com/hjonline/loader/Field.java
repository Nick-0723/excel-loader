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
    private String comment;

    public Field(String fieldName, String fieldType, String comment) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.comment = comment;
    }
}
