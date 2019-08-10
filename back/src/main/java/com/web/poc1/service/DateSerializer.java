package com.web.poc1.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class DateSerializer extends JsonSerializer<Date> {
    private SimpleDateFormat format = new SimpleDateFormat("YYYY/MM/dd");

    @Override
    public void serialize(final Date date, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
        String dateString = format.format(date);
        gen.writeString(dateString);
    }
}