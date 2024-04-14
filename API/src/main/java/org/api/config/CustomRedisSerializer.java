package org.api.config;

import static io.jsonwebtoken.lang.Objects.isEmpty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.DefaultBaseTypeLimitingValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class CustomRedisSerializer extends GenericJackson2JsonRedisSerializer {
    private final ObjectMapper objectMapper;

    public CustomRedisSerializer() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.activateDefaultTyping(new DefaultBaseTypeLimitingValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
    }

    @Override
    public byte[] serialize(Object source) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(source);
        } catch (Exception ex) {
            throw new SerializationException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Object deserialize(byte[] source) throws SerializationException {
        if (isEmpty(source)) {
            return null;
        }
        try {
            return objectMapper.readValue(source, Object.class);
        } catch (Exception ex) {
            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }
}
