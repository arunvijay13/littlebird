package com.littlebird.homeservice.mapper;


import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.util.Date;

public class DateToTimestampConverter implements Converter<Date, Timestamp> {

    @Override
    public Timestamp convert(Date source) {
        return (source != null) ? new Timestamp(source.getTime()) : null;
    }
}
