package com.example.hotsix.service.storage;

import com.example.hotsix.properties.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path uploadImgPath;

    @Autowired
    public FileSystemStorageService(StorageProperties storageProperties) {
        String path = storageProperties.uploadImages();

        this.uploadImgPath = Paths.get(path);
    }

    @Override
    public void store(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IOException("Failed to store empty file");

        Path path = this.uploadImgPath.resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();
        Path parentDir = path.getParent();

        if (!parentDir.equals(this.uploadImgPath.toAbsolutePath())) throw new IOException("Cannot store file outside current directory.");

        Files.createDirectories(parentDir);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public void remove(String fileName) throws IOException {
        Files.delete(uploadImgPath.resolve(fileName));
    }

    @Override
    public void replace(String fileName, MultipartFile toUpload) throws IOException {
        Files.delete(uploadImgPath.resolve(fileName));

        try (InputStream inputStream = toUpload.getInputStream()) {
            Files.copy(inputStream, uploadImgPath.resolve(toUpload.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Failed to store file " + toUpload.getOriginalFilename(), e);
        }
    }
}
