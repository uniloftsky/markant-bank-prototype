package net.uniloftsky.markant.bank.biz;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;

/**
 * Class holder for custom serializers
 */
public class BankSerializers {

    /**
     * Serializer for {@link AccountNumber}
     */
    public static class AccountNumberSerializer extends JsonSerializer<AccountNumber> {

        @Override
        public void serialize(AccountNumber accountNumber, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(accountNumber.toString());
        }
    }

    /**
     * Serializer for {@link TransactionId}
     */
    public static class TransactionIdSerializer extends JsonSerializer<TransactionId> {

        @Override
        public void serialize(TransactionId transactionId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(transactionId.toString());
        }
    }

    /**
     * Serializer for {@link Instant}
     */
    public static class InstantToMillisSerializer extends JsonSerializer<Instant> {

        @Override
        public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(instant.toEpochMilli());
        }
    }
}
