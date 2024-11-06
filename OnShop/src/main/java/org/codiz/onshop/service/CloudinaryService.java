package org.codiz.onshop.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {


        Map upload = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource-type", "image"));
        return upload.get("url").toString();

    }
    public String uploadVideo(MultipartFile file) throws IOException {
        Map upload = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource-type", "video"));
        return upload.get("url").toString();
    }
}
