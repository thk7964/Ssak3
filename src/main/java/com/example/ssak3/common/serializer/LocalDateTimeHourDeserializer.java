package com.example.ssak3.common.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 프론트에서 LocalDateTime을 시까지 입력 받으면 자동으로 분, 초를 00으로 세팅해줌 ("yyyy-MM-dd'T'HH" 형식)
 */
public class LocalDateTimeHourDeserializer extends StdDeserializer<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH");

    public LocalDateTimeHourDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext txt) throws IOException {
        try {
            return LocalDateTime.parse(p.getText(), FORMATTER);
        } catch (Exception e) {
            throw new IOException("startAt/endAt 형식이 올바르지 않습니다. yyyy-MM-dd'T'HH 형식으로 입력해주세요.");
        }
    }
}