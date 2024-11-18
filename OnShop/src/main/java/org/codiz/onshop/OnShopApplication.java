package org.codiz.onshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "org.codiz.onshop")
@EnableCaching
public class OnShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnShopApplication.class, args);
    }

}
