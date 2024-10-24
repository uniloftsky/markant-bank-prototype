package net.uniloftsky.markant.bank.biz;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Serializer for {@link TransactionId}
 */
public class TransactionIdSerializer extends JsonSerializer<TransactionId> {

    @Override
    public void serialize(TransactionId transactionId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(transactionId.toString());
    }
}
