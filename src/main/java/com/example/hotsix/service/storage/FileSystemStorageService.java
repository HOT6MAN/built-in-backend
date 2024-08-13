package com.example.hotsix.service.storage;

import com.example.hotsix.properties.ServerProperties;
import com.example.hotsix.properties.StorageProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileSystemStorageService implements StorageService {

    private final ServerProperties serverProperties;
    private final StorageProperties storageProperties;
    private Path uploadImagePath;

    @PostConstruct
    public void init() {
        String uploadImagePath = storageProperties.uploadImages();

        this.uploadImagePath = Paths.get(isUnix() ? convertUnixStyle(uploadImagePath) : uploadImagePath);
    }

    @Override
    public String getUploadedImageUrl(String imageName) {
        return String.format("%s%s/files/%s", serverProperties.origin(), serverProperties.servlet().contextPath() , imageName);
    }

    @Override
    public String getUploadedImagePath() {
        return this.uploadImagePath.toString();
    }

    @Override
    public void store(MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IOException("Failed to store empty file");

        Path path = uploadImagePath.resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();
        Path parentDir = path.getParent();

        if (!parentDir.equals(uploadImagePath.toAbsolutePath())) throw new IOException("Cannot store file outside current directory.");

        Files.createDirectories(parentDir);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public void remove(String fileName) throws IOException {
        if (fileName.isEmpty()) throw new IOException("Filename is null or empty");

        Files.delete(uploadImagePath.resolve(fileName));
    }

    @Override
    public void replace(String fileName, MultipartFile toUpload) throws IOException {
        if (fileName.isEmpty()) throw new IOException("Filename is null or empty");

        Files.delete(uploadImagePath.resolve(fileName));

        try (InputStream inputStream = toUpload.getInputStream()) {
            Files.copy(inputStream, uploadImagePath.resolve(toUpload.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Failed to store file " + toUpload.getOriginalFilename(), e);
        }
    }

    private boolean isUnix() {
        String osName = System.getProperty("os.name").toLowerCase();

        return Stream.of("nix", "nux", "mac").anyMatch(osName::contains);
    }

    private String convertUnixStyle(String path) {
        return path.replaceAll("^[A-Za-z]:", "");
    }
}
