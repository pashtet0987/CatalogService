package by.pashkavlushka.GoodsCatalogueService.controller;

import by.pashkavlushka.GoodsCatalogueService.dto.Category;
import by.pashkavlushka.GoodsCatalogueService.dto.CriteriaGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.dto.CriteriaGoodsResponse;
import by.pashkavlushka.GoodsCatalogueService.dto.Direction;
import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.dto.RecomendationDTO;
import by.pashkavlushka.GoodsCatalogueService.exception.EntityException;
import by.pashkavlushka.GoodsCatalogueService.exception.RecomendationServiceUnavailableException;
import by.pashkavlushka.GoodsCatalogueService.kafka.KafkaRecomendationServiceImpl;
import by.pashkavlushka.GoodsCatalogueService.service.GoodsService;
import by.pashkavlushka.GoodsCatalogueService.service.RecomendationsIntegrationService;
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
import org.springframework.data.domain.PageRequest;
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

    private final GoodsService goodsService;
    private final KafkaRecomendationServiceImpl kafkaService;
    private final RecomendationsIntegrationService recomendationService;

    @Autowired
    public GoodsController(GoodsService goodsService,
            KafkaRecomendationServiceImpl kafkaService,
            RecomendationsIntegrationService recomendationService
            ) {
        this.goodsService = goodsService;
        this.kafkaService = kafkaService;
        this.recomendationService = recomendationService;
    }

    @GetMapping("/get-categories")
    public List<String> findCategories() {
        return goodsService.findCategories();
    }

    @GetMapping("/{category}")
    public List<GoodsDTO> findByCategory(@PathVariable("category") String category, @RequestParam(name = "userId", required = false) Long userId) {
        if (userId != null) {
            kafkaService.addRecomendation(userId, category);
        }
        return goodsService.findByCategory(category);
    }

    @GetMapping("/")
    public List<GoodsDTO> recomendations(@RequestParam(name = "userId", required = false) Long userId, @RequestParam("page") int page) throws Exception {
        return recomendationService.getRecomendations(userId, page);
    }

    @GetMapping("/by-seller")
    public List<GoodsDTO> findBySeller(@RequestParam("sellerId") Long sellerId, @RequestParam("page") int page, @RequestParam(name = "direction", required = false) Direction direction, @RequestParam(name = "orderBy", required = false) String orderBy) {
        List<GoodsDTO> list;
        if(direction == null && orderBy == null) {
           list = goodsService.findBySellerId(sellerId, page);
        } else {
            list = goodsService.findBySellerId(sellerId, page, direction, orderBy);
        }
        
        return list;
    }

    @GetMapping("/goods")
    public GoodsDTO loadProductById(@RequestParam("itemId") long itemId) throws EntityException {
        return goodsService.findById(itemId);
    }

    //if no filters set show recomendations else show criteria
    @PostMapping("/")
    public CriteriaGoodsResponse productsByCriteria(@RequestParam(name = "userId", required = false) Long userId, @RequestBody CriteriaGoodsRequest goodsRequest) throws Exception {
        CriteriaGoodsResponse response;
        if ((goodsRequest.getNamePattern() == null || goodsRequest.getNamePattern().isBlank())
                && goodsRequest.getCategories().stream().filter(Category::isOn).count() == goodsRequest.getCategories().size()
                && (goodsRequest.getMaxCost() == null || goodsRequest.getMaxCost() == 0)
                && (goodsRequest.getMinCost()== null || goodsRequest.getMinCost() == 0)
                && (goodsRequest.getDirection() == Direction.ASC)
                && (goodsRequest.getOrderBy().equals("cost"))) {
            List<GoodsDTO> goods = recomendationService.getRecomendations(userId, goodsRequest.getPageNumber());
            
            response = new CriteriaGoodsResponse();
            response.setGoods(goods);
            response.setPageNumber(goodsRequest.getPageNumber());
            response.setCategories(goodsRequest.getCategories());
            //direction and orderby are set once customer goes to main page
            response.setDirection(goodsRequest.getDirection());
            response.setOrderBy(goodsRequest.getOrderBy());
            response.setHasNextPage(goodsService.hasNextPage(goods));
        } else {
            response = goodsService.fulfillCriteriaRequest(goodsRequest);
            response.setHasNextPage(goodsService.hasNextPage(response.getGoods()));
        }
        return response;
    }

}
