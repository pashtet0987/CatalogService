package by.pashkavlushka.GoodsCatalogueService.service;

import by.pashkavlushka.GoodsCatalogueService.dto.AddToCartRequest;
import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.dto.RecomendationDTO;
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
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.hibernate.Session;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@Service
public class GoodsServiceImpl implements GoodsService {

    private final Pageable defaultPageable;
    private final GoodsRepository goodsRepository;
    private final GoodsMapper parser;
    private final EntityManagerFactory entityManagerFactory;

    public GoodsServiceImpl(GoodsRepository goodsRepository, GoodsMapper parser, EntityManagerFactory entityManagerFactory) {
        this.defaultPageable = PageRequest.of(0, 30, Sort.by(List.of(Sort.Order.asc("cost"))));
        this.goodsRepository = goodsRepository;
        this.parser = parser;
        this.entityManagerFactory = entityManagerFactory;
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
    public List<GoodsDTO> findBySellerId(long sellerId) {
        return findBySellerIdDefault(sellerId, defaultPageable);
    }

    @Transactional
    public List<GoodsDTO> findBySellerId(long sellerId, int pageNum) {
        return findBySellerIdDefault(sellerId, defaultPageable.withPage(pageNum));
    }

    @Transactional
    public List<GoodsDTO> findBySellerId(long sellerId, Pageable pageable) {
        return findBySellerIdDefault(sellerId, pageable);
    }

    private List<GoodsDTO> findBySellerIdDefault(long sellerId, Pageable pageable) {
        List<GoodsEntity> entities = goodsRepository.findBySellerId(sellerId, pageable).getContent();
        return entities.stream().map((entity) -> parser.entityToDTO(entity)).toList();
    }

    @Override
    public boolean addToCart(Long itemId, int amount) {
        while (true) {
            try {
                return addToCartInner(itemId, amount);
            } catch (ObjectOptimisticLockingFailureException | OptimisticLockException e) {
                System.out.println(e.getMessage());
                //no need to do anything so that the algorithm works again
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
    }

    private boolean addToCartInner(Long itemId, int amount) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
      
        try {
            //GoodsEntity entity = entityManager.find(GoodsEntity.class, itemId);
            GoodsEntity entity = entityManager.find(GoodsEntity.class, itemId, LockModeType.PESSIMISTIC_READ);
            if (entity == null) {
                throw new NotFoundEntityException();
            }
            entityManager.lock(entity, LockModeType.NONE);
            if (entity.getAmount() - amount < 0) {
                return false;
            }
            entity.setAmount(entity.getAmount() - amount);
            entityManager.lock(entity, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            entityManager.merge(entity);
            entityManager.flush();
            entityManager.lock(entity, LockModeType.NONE);
            transaction.commit();
            return true;
        } catch (Exception e) {
            transaction.rollback();
            entityManager.close();
            return false;
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            entityManager.close();
        }
    }

    @Override
    @Transactional
    public AddToCartRequest validateAddToCartRequest(AddToCartRequest request) throws EntityException {
        goodsRepository.findById(request.getItemId()).orElseThrow(() -> new NotFoundEntityException());
        request.setStatus(true);
        return request;
    }

    @Override
    @Transactional
    public List<GoodsDTO> findByRecomendations(List<RecomendationDTO> recomendations) {
        List<String> categories = recomendations.stream().map(RecomendationDTO::getCategory).toList();
        
        return goodsRepository.findByCategories(categories,defaultPageable)
                .getContent().stream().map(parser::entityToDTO).toList();
    }

}
