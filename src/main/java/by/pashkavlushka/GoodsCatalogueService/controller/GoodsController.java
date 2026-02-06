package by.pashkavlushka.GoodsCatalogueService.controller;

import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.dto.RecomendationDTO;
import by.pashkavlushka.GoodsCatalogueService.exception.EntityException;
import by.pashkavlushka.GoodsCatalogueService.exception.RecomendationServiceUnavailableException;
import by.pashkavlushka.GoodsCatalogueService.kafka.KafkaRecomendationServiceImpl;
import by.pashkavlushka.GoodsCatalogueService.service.GoodsService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.decorators.Decorators;
import java.nio.channels.ClosedChannelException;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping
public class GoodsController {

    private GoodsService goodsService;
    private RestClient restClient;
    private KafkaRecomendationServiceImpl kafkaService;
    private String recomendationsServiceUrl;
    private final CircuitBreaker circuitBreaker;

    @Autowired
    public GoodsController(GoodsService goodsService,
            RestClient restClient,
            KafkaRecomendationServiceImpl kafkaService,
            CircuitBreaker circuitBreaker,
            @Value("${services.recomendations.url}") String recomendationsServiceUrl) {
        this.goodsService = goodsService;
        this.restClient = restClient;
        this.kafkaService = kafkaService;
        this.recomendationsServiceUrl = recomendationsServiceUrl;
        this.circuitBreaker = circuitBreaker;
    }

    @GetMapping("/{category}")
    public List<GoodsDTO> findByCategory(@PathVariable("category") String category, @RequestParam("userId") long userId) {
        kafkaService.addRecomendation(userId, category);
        return goodsService.findByCategory(category);
    }

    @GetMapping("/")
    public List<GoodsDTO> recomendations(@RequestParam("userId") long userId) throws Exception {
        return Decorators.ofCallable(()
                -> {
            try {
                List<RecomendationDTO> recomendations = restClient
                        .get()
                        .uri(String.format("%s/recomendations/?userId=%d", recomendationsServiceUrl, userId))
                        .retrieve()
                        .toEntity(new ParameterizedTypeReference<List<RecomendationDTO>>() {
                        })
                        .getBody();
                return goodsService.findByRecomendations(recomendations);
            } catch (Exception e) {
                throw new RecomendationServiceUnavailableException();
            }
        })
        .withCircuitBreaker(circuitBreaker)
        .withFallback(Exception.class, (ex) -> goodsService.findForFallback())
        .call();

    }

    //также получать параметр номера страницы для пагинации
    @GetMapping("/by-seller")
    public List<GoodsDTO> findBySeller(@RequestParam("sellerId") Long sellerId) {
        return goodsService.findBySellerId(sellerId);
    }
    
    @GetMapping("/goods")
    public GoodsDTO loadProductById(@RequestParam("itemId") long itemId) throws EntityException {
        return goodsService.findById(itemId);
    }
    
}
