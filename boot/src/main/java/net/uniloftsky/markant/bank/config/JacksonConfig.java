package net.uniloftsky.markant.bank.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import net.uniloftsky.markant.bank.biz.AccountNumber;
import net.uniloftsky.markant.bank.biz.BankSerializers;
import net.uniloftsky.markant.bank.biz.TransactionId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Configuration for Jackson
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // register custom serializers/deserializers
        SimpleModule module = new SimpleModule();
        module.addSerializer(AccountNumber.class, new BankSerializers.AccountNumberSerializer());
        module.addSerializer(BigDecimal.class, new ToStringSerializer());
        module.addSerializer(TransactionId.class, new BankSerializers.TransactionIdSerializer());
        module.addSerializer(Instant.class, new BankSerializers.InstantToMillisSerializer());
        objectMapper.registerModule(module);

        return objectMapper;
    }

}
