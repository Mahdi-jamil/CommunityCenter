package com.devesta.blogify.firebase;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FireBaseConfig {
    @Bean
    public Storage getFirebaseAccessStorage(){
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("Firebase-connectivity.json")) {
            assert inputStream != null;
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
