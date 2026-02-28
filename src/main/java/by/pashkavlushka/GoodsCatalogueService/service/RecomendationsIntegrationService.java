package by.pashkavlushka.GoodsCatalogueService.service;

import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.dto.RecomendationDTO;
import by.pashkavlushka.GoodsCatalogueService.exception.RecomendationServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class RecomendationsIntegrationService  {

    private GoodsService goodsService;
    private RestClient restClient;
    private String recomendationsServiceUrl;
    private final CircuitBreaker circuitBreaker;
    private final int defaultPageSize;

    public RecomendationsIntegrationService(GoodsService goodsService, RestClient restClient, @Value("${services.recomendations.url}") String recomendationsServiceUrl, CircuitBreaker circuitBreaker, @Value("${goods.page.size:30}") int defaultPageSize) {
        this.goodsService = goodsService;
        this.restClient = restClient;
        this.recomendationsServiceUrl = recomendationsServiceUrl;
        this.circuitBreaker = circuitBreaker;
        this.defaultPageSize = defaultPageSize;
    }
    
    //uses Recomendation service to get top categories for user
    //if service is not available or categories list is empty
    //returns products by 5 random categories
    public List<GoodsDTO> getRecomendations(Long userId, int page) throws Exception {
        return Decorators.ofCallable(()
                -> {
            try {
                if (userId != null) {
                    List<RecomendationDTO> recomendations = restClient
                            .get()
                            .uri(String.format("%s/recomendations/?userId=%d", recomendationsServiceUrl, userId))
                            .retrieve()
                            .toEntity(new ParameterizedTypeReference<List<RecomendationDTO>>() {
                            })
                            .getBody();
                    if (recomendations != null && !recomendations.isEmpty()) {
                        return goodsService.findByRecomendations(recomendations, page);
                    }
                }
                return goodsService.findForFallback(page);
            } catch (Exception e) {
                throw new RecomendationServiceUnavailableException();
            }
        })
                .withCircuitBreaker(circuitBreaker)
                .withFallback(Exception.class, (ex) -> goodsService.findForFallback(page))
                .call();
    }
    
}
