package org.codiz.onshop.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codiz.onshop.dtos.requests.FileUploads;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String uploadImage(FileUploads file) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getFileData())){
            byte[] image = convertInputStreamToByteArray(inputStream);

            // Upload the file to Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    image,
                    ObjectUtils.asMap(
                            "resource_type", "auto",
                            "folder", "product_images",
                            "use_filename", true,
                            "unique_filename", true
                    )
            );
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            log.error("Error uploading image to Cloudinary: " + e.getMessage(), e);
            throw new RuntimeException("Failed to upload image to Cloudinary", e);
        }
    }

    public String uploadVideo(FileUploads file) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getFileData())){
            Map<String, Object> upload = cloudinary.uploader().upload(inputStream,
                    ObjectUtils.asMap("resource-type", "video",
                            "folder", "product_videos",
                            "user_filename", true,
                            "unique_filename", false));
            return upload.get("url").toString();
        }catch (IOException e){
            log.error("Error uploading video to Cloudinary: " + e.getMessage(), e);
            throw new RuntimeException("Failed to upload video to Cloudinary", e);
        }
    }

    public boolean deleteImage(String url) throws IOException {
        String publicId = extractPublicIdFromUrl(url);
        Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        log.info("deleted successfully: " + result);
        return "ok".equals(result.get("status"));
    }

    private String extractPublicIdFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }

        int lastSlashIndex = url.lastIndexOf("/");
        int lastDotIndex = url.lastIndexOf(".");

        if (lastSlashIndex == -1 || lastDotIndex == -1 || lastSlashIndex >= lastDotIndex) {
            throw new IllegalArgumentException("Invalid URL format: " + url);
        }
        log.info("the public id is:"+url.substring(lastSlashIndex+1, lastDotIndex));

        return url.substring(lastSlashIndex + 1, lastDotIndex);
    }


    public byte[] convertInputStreamToByteArray(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        }
    }
}
