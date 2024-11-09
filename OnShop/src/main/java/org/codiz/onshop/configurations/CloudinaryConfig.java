package org.codiz.onshop.configurations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary-name}")
    private String cloudinaryName;
    @Value("${cloudinary-api-key}")
    private String cloudinaryApiKey;
    @Value("${cloudinary-secret-key}")
    private String cloudinarySecretKey;
    @Bean
    public Cloudinary cloudinary() {

        return new Cloudinary(ObjectUtils.asMap(
        "cloud_name", cloudinaryName,
                "api_key", cloudinaryApiKey,
                "api_secret", cloudinarySecretKey
        ));
    }


}
