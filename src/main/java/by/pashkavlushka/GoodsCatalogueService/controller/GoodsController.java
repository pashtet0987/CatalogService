package by.pashkavlushka.GoodsCatalogueService.controller;

import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.dto.RecomendationDTO;
import by.pashkavlushka.GoodsCatalogueService.kafka.KafkaService;
import by.pashkavlushka.GoodsCatalogueService.service.GoodsService;
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
    private KafkaService kafkaService;
    private String recomendationsServiceUrl;

    @Autowired
    public GoodsController(GoodsService goodsService, RestClient restClient, KafkaService kafkaService, @Value("${services.recomendations.url}") String recomendationsServiceUrl) {
        this.goodsService = goodsService;
        this.restClient = restClient;
        this.kafkaService = kafkaService;
        this.recomendationsServiceUrl = recomendationsServiceUrl;
    }
    
    @GetMapping("/{category}")
    public List<GoodsDTO> findByCategory(@PathVariable("category") String category, @RequestParam("userId") long userId){
        kafkaService.addRecomendation(userId, category);
        return goodsService.findByCategory(category);
    }
    
    @GetMapping("/")
    //сделать circuit breaker и fallback(дефолтный ответ)
    public List<GoodsDTO> recomendations(@RequestParam("userId") long userId){
        List<RecomendationDTO> recomendations = restClient
                .get()
                .uri(String.format("http://%s/recomendations/?userId=%d", recomendationsServiceUrl, userId))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<RecomendationDTO>>(){})
                .getBody();
        return goodsService.findByRecomendations(recomendations);
    }
    
    @GetMapping("/by-seller")
    public List<GoodsDTO> findBySeller(@RequestParam("sellerId") Long sellerId){
        return goodsService.findBySellerId(sellerId);
    }
}
