package com.devesta.blogify.firebase;

import com.devesta.blogify.exception.exceptions.BadRequestException;
import com.devesta.blogify.exception.exceptions.FileNotFoundException;
import com.google.cloud.storage.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Component
public class FileDAO {

    private final Storage storage;

    public String uploadAndGetUrl(MultipartFile image) throws IOException {
        if (!isImage(image))
            throw new BadRequestException("Only .jpeg, .jpg, or .png image files are allowed.");

        String fileName = UUID.randomUUID().toString().concat(
                this.getExtension(Objects.requireNonNull(image.getOriginalFilename())));
        File file = convertToFile(image, fileName);

        return uploadFile(file, fileName);
    }

    public Blob getFileFromFirebase(String fileUrl) {
        String fileName = extractFileNameFromUrl(fileUrl);
        BlobId blobId = BlobId.of("community-center-db.appspot.com", fileName);
        Blob file = storage.get(blobId);
        if (file != null) return file;
        throw new FileNotFoundException("File not found on Firebase");
    }

    public void deleteFileFromFirebase(String fileUrl) {
        String fileName = extractFileNameFromUrl(fileUrl);
        BlobId blobId = BlobId.of("community-center-db.appspot.com", fileName);
        try {
            storage.delete(blobId);
        } catch (StorageException e) {
            throw new RuntimeException("Failed to delete file from Firebase Storage: " + e.getMessage());
        }
    }

    private String uploadFile(File file, String fileName) {
        BlobId blobId = BlobId.of("community-center-db.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        try {
            storage.create(blobInfo, Files.readAllBytes(file.toPath()));
            String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/community-center-db.appspot.com/o/%s?alt=media";
            return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractFileNameFromUrl(String imageUrl) {
        int questionMarkIndex = imageUrl.indexOf('?');
        if (questionMarkIndex != -1) {
            imageUrl = imageUrl.substring(0, questionMarkIndex);
        }
        int lastSlashIndex = imageUrl.lastIndexOf('/');
        if (lastSlashIndex != -1 && lastSlashIndex < imageUrl.length() - 1) {
            return imageUrl.substring(lastSlashIndex + 1);
        } else {
            throw new IllegalArgumentException("Invalid image URL format");
        }
    }

    private boolean isImage(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png");
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = Files.createTempFile(fileName, ".tmp").toFile();
        multipartFile.transferTo(tempFile);
        tempFile.deleteOnExit();
        return tempFile;
    }

}
