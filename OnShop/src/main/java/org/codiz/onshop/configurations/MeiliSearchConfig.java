package org.codiz.onshop.configurations;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeiliSearchConfig {

    @Value("${meilisearch.url}")
    private String meiliSearchUrl;

    @Bean
    public Client meiliSearchClient() {
        return new Client(new Config(meiliSearchUrl, null)); // No API key needed
    }
}
