package org.codiz.onshop.repositories.products;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.SearchResult;
import org.codiz.onshop.dtos.requests.ProductDocument;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class ProductSearchRepository {

    private final Client meiliSearchClient;
    private final ObjectMapper objectMapper;
    private final String indexName = "products"; // MeiliSearch index name

    public ProductSearchRepository(Client meiliSearchClient, ObjectMapper objectMapper) {
        this.meiliSearchClient = meiliSearchClient;
        this.objectMapper = objectMapper;
    }

    public void saveAll(List<ProductDocument> products) {
        try {
            String productsJson = objectMapper.writeValueAsString(products);
            meiliSearchClient.index(indexName).addDocuments(productsJson);
        } catch (JsonProcessingException | RuntimeException e) {
            e.printStackTrace(); // Consider proper logging
        }
    }

    public List<ProductDocument> search(String query) {
        try {
            SearchRequest searchRequest = new SearchRequest(query).setLimit(10);
            SearchResult searchResult = (SearchResult) meiliSearchClient.index(indexName).search(searchRequest);

            // Convert JSON array response to list of ProductDocument objects
            ProductDocument[] products = objectMapper.readValue(searchResult.getHits().toString(), ProductDocument[].class);
            return Arrays.asList(products);
        } catch (RuntimeException | JsonProcessingException e) {
            e.printStackTrace(); // Consider proper logging
            return List.of();
        }
    }
}
