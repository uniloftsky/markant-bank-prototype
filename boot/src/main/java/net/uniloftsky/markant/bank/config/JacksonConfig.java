package net.uniloftsky.markant.bank.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import net.uniloftsky.markant.bank.biz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Configuration for jackson mapping
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // register custom serializers
        SimpleModule customSerializersModule = new SimpleModule();
        customSerializersModule.addSerializer(AccountNumber.class, new AccountNumberSerializer());
        customSerializersModule.addSerializer(BigDecimal.class, new ToStringSerializer());
        customSerializersModule.addSerializer(TransactionId.class, new TransactionIdSerializer());
        customSerializersModule.addSerializer(Instant.class, new InstantToMillisSerializer());
        objectMapper.registerModule(customSerializersModule);

        return objectMapper;
    }

}
