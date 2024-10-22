package net.uniloftsky.markant.bank.biz;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Serializer for {@link AccountNumber}
 */
public class AccountNumberSerializer extends JsonSerializer<AccountNumber> {

    @Override
    public void serialize(AccountNumber accountNumber, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(accountNumber.toString());
    }
}
