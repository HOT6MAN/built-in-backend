package com.example.hotsix.service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    void store(MultipartFile file) throws IOException;

    void remove(String fileName) throws IOException;

    void replace(String fileName, MultipartFile toUpload) throws IOException;
}
