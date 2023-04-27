package com.gitoshh.rideshare.OnboardingService.contracts;

import java.io.InputStream;
import java.util.Map;

public interface FileStorageInterface {
    String uploadFile(String fileName, InputStream inputStream, Map<String, String> objectMetadata);

    byte[] getFile(String fileName);
}
