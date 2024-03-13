package org.api.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Instant> {
    @Override
    public Instant convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.toInstant();
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Instant instant) {
        return instant == null ? null : instant.atZone(ZoneId.of("Asia/Seoul"));
    }
}
