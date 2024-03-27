package com.devesta.blogify.user.domain.profile;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

@JsonComponent
public class BlobSerializer extends JsonSerializer<Blob> {

    @Override
    public void serialize(Blob blob, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try {
            int blobLength = (int) blob.length();
            byte[] blobAsBytes = blob.getBytes(1, blobLength);
            jsonGenerator.writeObject(blobAsBytes);
        } catch (SQLException e) {
            jsonGenerator.writeNull();
        }
    }
}

