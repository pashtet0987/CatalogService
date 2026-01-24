/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.GoodsCatalogueService.service;

import by.pashkavlushka.GoodsCatalogueService.dto.AddToCartRequest;
import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.entity.GoodsEntity;
import by.pashkavlushka.GoodsCatalogueService.exception.EntityException;
import by.pashkavlushka.GoodsCatalogueService.exception.NotFoundEntityException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface GoodsService {

    GoodsDTO findById(Long id) throws EntityException;

    List<GoodsDTO> findByCategory(String category);

    List<GoodsDTO> findByCategory(String category, int pageNum);

    List<GoodsDTO> findByCategory(String category, Pageable pageable);
    
    List<GoodsDTO> findBySellerId(String sellerId);
    
    List<GoodsDTO> findBySellerId(String sellerId, int pageNum);
    
    List<GoodsDTO> findBySellerId(String sellerId, Pageable pageable);

    GoodsDTO save(GoodsDTO goodsDTO);
    
    boolean addToCart(Long itemId, int amount);
    
    AddToCartRequest createAddToCartRequest(Long cartId, Long itemId, int amount) throws EntityException;
}
