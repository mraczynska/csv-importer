package com.decent.csvimporter.generator;

import org.csveed.bean.conversion.AbstractConverter;

import java.util.regex.Pattern;

public class EmailValidator extends AbstractConverter<String> {

    private static final Pattern EMAIL_REGEXP = Pattern.compile("^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,3})+)$");

    public EmailValidator() {
        super(String.class);
    }

    @Override
    public String fromString(String text) {
        if (EMAIL_REGEXP.matcher(text).matches()) {
            return text;
        }
        throw new IllegalArgumentException("Email doesn't match regexp");
    }

    @Override
    public String toString(String value) {
        return value;
    }
}
