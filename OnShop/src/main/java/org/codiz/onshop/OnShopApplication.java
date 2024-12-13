package org.codiz.onshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "org.codiz.onshop")
@EnableCaching
@EnableAsync
public class OnShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnShopApplication.class, args);
    }

}
