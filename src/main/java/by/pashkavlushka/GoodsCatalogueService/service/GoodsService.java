package by.pashkavlushka.GoodsCatalogueService.service;

import by.pashkavlushka.GoodsCatalogueService.dto.AddGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.dto.AddToCartRequest;
import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.dto.RecomendationDTO;
import by.pashkavlushka.GoodsCatalogueService.dto.UpdateGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.entity.GoodsEntity;
import by.pashkavlushka.GoodsCatalogueService.exception.EntityException;
import by.pashkavlushka.GoodsCatalogueService.exception.NotFoundEntityException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface GoodsService {

    GoodsDTO findById(Long id) throws EntityException;

    List<GoodsDTO> findByCategory(String category);

    List<GoodsDTO> findByCategory(String category, int pageNum);

    List<GoodsDTO> findByCategory(String category, Pageable pageable);
    
    List<GoodsDTO> findBySellerId(long sellerId);
    
    List<GoodsDTO> findBySellerId(long sellerId, int pageNum);
    
    List<GoodsDTO> findBySellerId(long sellerId, Pageable pageable);

    GoodsDTO save(GoodsDTO goodsDTO);
    
    boolean addToCart(Long itemId, int amount);
    
    AddToCartRequest validateAddToCartRequest(AddToCartRequest request) throws EntityException;

    List<GoodsDTO> findByRecomendations(List<RecomendationDTO> recomendations);
    
    List<GoodsDTO> findForFallback();
    
    void updateInventory(UpdateGoodsRequest request, Acknowledgment ack);
    
    void addToInventory(AddGoodsRequest dto, Acknowledgment ack);
}
