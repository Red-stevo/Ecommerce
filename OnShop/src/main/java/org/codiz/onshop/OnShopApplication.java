package org.codiz.onshop;

import io.vanslog.spring.data.meilisearch.repository.config.EnableMeilisearchRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.codiz.onshop")

@EnableMeilisearchRepositories(basePackages = "org.codiz.onshop.repositories.products")
public class OnShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnShopApplication.class, args);
    }

}
