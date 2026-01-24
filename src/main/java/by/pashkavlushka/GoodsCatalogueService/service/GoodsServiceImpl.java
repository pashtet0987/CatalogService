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
import by.pashkavlushka.GoodsCatalogueService.repository.GoodsRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import by.pashkavlushka.GoodsCatalogueService.mapstruct.GoodsMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;

@Service
public class GoodsServiceImpl implements GoodsService {

    private final Pageable defaultPageable;
    private final GoodsRepository goodsRepository;
    private final GoodsMapper parser;
    private final EntityManager entityManager;

    public GoodsServiceImpl(GoodsRepository goodsRepository, GoodsMapper parser, EntityManager entityManager) {
        this.defaultPageable = PageRequest.of(0, 30, Sort.by(List.of(Sort.Order.asc("cost"))));
        this.goodsRepository = goodsRepository;
        this.parser = parser;
        this.entityManager = entityManager;
    }

    @Transactional
    public GoodsDTO findById(Long id) throws EntityException {
        GoodsEntity entity = goodsRepository.findById(id).orElseThrow(() -> new NotFoundEntityException("Could not find entity"));
        return parser.entityToDTO(entity);
    }

    @Transactional
    public List<GoodsDTO> findByCategory(String category) {
        return findByCategory(category, defaultPageable);
    }

    @Transactional
    public List<GoodsDTO> findByCategory(String category, int pageNum) {
        return findByCategory(category, defaultPageable.withPage(pageNum));
    }

    @Transactional
    public List<GoodsDTO> findByCategory(String category, Pageable pageable) {
        List<GoodsEntity> entities = goodsRepository.findByCategory(category, pageable).getContent();
        return entities.stream().map((entity) -> parser.entityToDTO(entity)).toList();
    }

    private List<GoodsDTO> findByCategoryDefault(String category, Pageable pageable) {
        List<GoodsEntity> entities = goodsRepository.findByCategory(category, pageable).getContent();
        return entities.stream().map((entity) -> parser.entityToDTO(entity)).toList();
    }

    @Transactional
    public GoodsDTO save(GoodsDTO goodsDTO) {
        return parser.entityToDTO(goodsRepository.save(parser.dtoToEntity(goodsDTO)));
    }

    @Transactional
    public List<GoodsDTO> findBySellerId(String sellerId) {
        return findBySellerIdDefault(sellerId, defaultPageable);
    }

    @Transactional
    public List<GoodsDTO> findBySellerId(String sellerId, int pageNum) {
        return findBySellerIdDefault(sellerId, defaultPageable.withPage(pageNum));
    }

    @Transactional
    public List<GoodsDTO> findBySellerId(String sellerId, Pageable pageable) {
        return findBySellerIdDefault(sellerId, pageable);
    }

    private List<GoodsDTO> findBySellerIdDefault(String sellerId, Pageable pageable) {
        List<GoodsEntity> entities = goodsRepository.findBySellerId(sellerId, pageable).getContent();
        return entities.stream().map((entity) -> parser.entityToDTO(entity)).toList();
    }

    @Override
    @Transactional
    public boolean addToCart(Long itemId, int amount) {
        while (true) {
            try {
                return addToCartInner(itemId, amount);
            } catch (OptimisticLockException e) {
                //no need to do anything so that the algorithm works again
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
    }

    private boolean addToCartInner(Long itemId, int amount) throws EntityException {
        GoodsEntity entity = goodsRepository.findById(itemId).orElseThrow(() -> new NotFoundEntityException());
        if (entity.getAmount() - amount < 0) {
            return false;
        }
        entity.setAmount(entity.getAmount() - amount);
        entityManager.refresh(entity, LockModeType.OPTIMISTIC);
        return true;
    }

    @Override
    @Transactional
    public AddToCartRequest createAddToCartRequest(Long cartId, Long itemId, int amount) throws EntityException {
        GoodsEntity entity = goodsRepository.findById(itemId).orElseThrow(()->new NotFoundEntityException());
        
        return new AddToCartRequest(cartId, itemId, entity.getName(), entity.getCost(), amount);
    }

    
    
}
