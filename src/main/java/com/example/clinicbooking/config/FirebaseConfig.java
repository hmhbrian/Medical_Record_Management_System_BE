package com.example.clinicbooking.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Collections;

@Configuration
public class FirebaseConfig {
    @Value("${app.firebase-config}")
    private String firebaseConfigPath;

    @Value("${app.firebase-project-id}")
    private String firebaseProjectId;

    @PostConstruct
    public void initialize() {
        try {
            Resource resource = new ClassPathResource(firebaseConfigPath);
//            FileInputStream serviceAccount =
//                    new FileInputStream("path/to/serviceAccountKey.json");

            // Nếu đã khởi tạo, không làm gì nữa
            if (FirebaseApp.getApps().isEmpty()) {
                //Resource resource = new ClassPathResource("firebase-service-account.json");

                // 🚨 ĐỊNH NGHĨA SCOPE TƯỜNG MINH CHO FCM 🚨
                GoogleCredentials credentials = GoogleCredentials
                        .fromStream(resource.getInputStream())
                        // Scope cho phép truy cập Firebase Cloud Messaging API
                        .createScoped(Collections.singletonList(
                                "https://www.googleapis.com/auth/firebase.messaging"
                        ));

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .setProjectId(firebaseProjectId)
                        .build();
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Admin SDK initialized successfully.");
            }
        } catch (IOException e) {
            System.err.println("Error initializing Firebase Admin SDK: " + e.getMessage());
            e.printStackTrace();
            // Ném Runtime Exception để Spring biết rằng ứng dụng không thể chạy
            throw new RuntimeException("Firebase initialization failed, application cannot start.", e);
        }
    }
}
