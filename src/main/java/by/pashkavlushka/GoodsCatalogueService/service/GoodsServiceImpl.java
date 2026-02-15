package by.pashkavlushka.GoodsCatalogueService.service;

import by.pashkavlushka.GoodsCatalogueService.dto.AddGoodsFeedback;
import by.pashkavlushka.GoodsCatalogueService.dto.AddGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.dto.AddToCartRequest;
import by.pashkavlushka.GoodsCatalogueService.dto.DeleteGoodsFeedback;
import by.pashkavlushka.GoodsCatalogueService.dto.DeleteGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.dto.RecomendationDTO;
import by.pashkavlushka.GoodsCatalogueService.dto.UpdateGoodsFeedback;
import by.pashkavlushka.GoodsCatalogueService.dto.UpdateGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.entity.GoodsEntity;
import by.pashkavlushka.GoodsCatalogueService.entity.GoodsToRollbackUpdateEntity;
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
import by.pashkavlushka.GoodsCatalogueService.mapstruct.GoodsRollbackMapper;
import by.pashkavlushka.GoodsCatalogueService.mapstruct.UpdateRequestMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import by.pashkavlushka.GoodsCatalogueService.repository.RollbackUpdateEventRepository;

@Service
public class GoodsServiceImpl implements GoodsService {

    private final Pageable defaultPageable;
    private final GoodsRepository goodsRepository;
    private final GoodsMapper goodsMapper;
    private final EntityManagerFactory entityManagerFactory;
    private final int defaultPageSize;
    private final UpdateRequestMapper updateRequestMapper;
    private final GoodsRollbackMapper goodsRollbackMapper;
    private final RollbackUpdateEventRepository rollbackUpdateRepository;

    public GoodsServiceImpl(GoodsRepository goodsRepository,
            GoodsMapper parser,
            EntityManagerFactory entityManagerFactory,
            @Value("${goods.page.size:30}") int defaultPageSize,
            UpdateRequestMapper updateRequestMapper,
            RollbackUpdateEventRepository rollbackUpdateRepository,
            GoodsRollbackMapper goodsRollbackMapper) {
        this.defaultPageable = PageRequest.of(0, defaultPageSize, Sort.by(List.of(Sort.Order.asc("cost"))));
        this.goodsRepository = goodsRepository;
        this.goodsMapper = parser;
        this.entityManagerFactory = entityManagerFactory;
        this.defaultPageSize = defaultPageSize;
        this.updateRequestMapper = updateRequestMapper;
        this.rollbackUpdateRepository = rollbackUpdateRepository;
        this.goodsRollbackMapper = goodsRollbackMapper;
    }

    @Transactional
    public GoodsDTO findById(Long id) throws EntityException {
        GoodsEntity entity = goodsRepository.findById(id).orElseThrow(() -> new NotFoundEntityException("Could not find entity"));
        return goodsMapper.entityToDTO(entity);
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
        return entities.stream().map((entity) -> goodsMapper.entityToDTO(entity)).toList();
    }

    private List<GoodsDTO> findByCategoryDefault(String category, Pageable pageable) {
        List<GoodsEntity> entities = goodsRepository.findByCategory(category, pageable).getContent();
        return entities.stream().map((entity) -> goodsMapper.entityToDTO(entity)).toList();
    }

    @Transactional
    public GoodsDTO save(GoodsDTO goodsDTO) {
        return goodsMapper.entityToDTO(goodsRepository.save(goodsMapper.dtoToEntity(goodsDTO)));
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
        return entities.stream().map((entity) -> goodsMapper.entityToDTO(entity)).toList();
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
        GoodsEntity entity = goodsRepository.findById(request.getItemId()).orElseThrow(() -> new NotFoundEntityException());
        request.setCost(entity.getCost());
        request.setItemName(entity.getName());
        if (rollbackUpdateRepository.findInFlightUpdatesByItemId(request.getItemId()) == 0) {
            request.setStatus(true);
        }

        return request;
    }

    @Override
    @Transactional
    public List<GoodsDTO> findByRecomendations(List<RecomendationDTO> recomendations) {
        List<String> categories = recomendations.stream().map(RecomendationDTO::getCategory).toList();

        return goodsRepository.findByCategories(categories, defaultPageable)
                .getContent().stream().map(goodsMapper::entityToDTO).toList();
    }

    //used in fallback method, chooses by 5 random categories
    @Override
    public List<GoodsDTO> findForFallback() {
        List<String> categories = goodsRepository.findCategories();
        Collections.shuffle(categories);
        categories = categories.stream().limit(5).toList();
        List<GoodsEntity> result = goodsRepository.findByCategories(categories);
        Collections.shuffle(result);
        return result.stream().limit(defaultPageSize).map(goodsMapper::entityToDTO).toList();
    }

    @Override
    @Transactional
    public AddGoodsFeedback addToInventory(AddGoodsRequest dto, Acknowledgment acknowledgment) {
        if (goodsRepository.save(goodsMapper.requestToEntity(dto)).getId() > 0) {
            acknowledgment.acknowledge();
            return new AddGoodsFeedback(dto.getId(), true, false);
        }
        return new AddGoodsFeedback(dto.getId(), false, true);
    }

    //нужно обновлять данные цены в корзинах или не хранить цену в корзине
    @Override
    public UpdateGoodsFeedback updateInventory(UpdateGoodsRequest updateRequest, Acknowledgment ack) {
        while (true) {
            try {
                GoodsToRollbackUpdateEntity rollbackUpdateEntity = updateInventoryInner(updateRequest);
                rollbackUpdateEntity.setId(updateRequest.getId());
                rollbackUpdateRepository.save(rollbackUpdateEntity);
                ack.acknowledge();
                return new UpdateGoodsFeedback(updateRequest.getId(), true, false);
            } catch (ObjectOptimisticLockingFailureException | OptimisticLockException e) {
                System.out.println(e.getMessage());
                //no need to do anything so that the algorithm works again
            } catch (NotFoundEntityException e) {
                return new UpdateGoodsFeedback(updateRequest.getId(), false, false);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new UpdateGoodsFeedback(updateRequest.getId(), false, true);
            }
        }
    }

    private GoodsToRollbackUpdateEntity updateInventoryInner(UpdateGoodsRequest updateRequest) throws NotFoundEntityException {
        EntityManager session = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = session.getTransaction();
        transaction.begin();
        try {
            GoodsEntity entity = session.find(GoodsEntity.class, updateRequest.getItemId());
            if (entity == null) {
                throw new NotFoundEntityException();
            }
            if (entity.getSellerId() == updateRequest.getSellerId()) {
                //сохраняем старую версию
                //amount кладется в toAddAmount
                GoodsToRollbackUpdateEntity rollbackUpdateEntity = goodsRollbackMapper.entityToRollbackEntity(entity);
                
                //добавляем количество к текущему
                session.lock(entity, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
                entity.setAmount(entity.getAmount() + updateRequest.getToAddAmount());
                entity.setCost(updateRequest.getCost());
                entity.setCategory(updateRequest.getCategory());
                entity.setCharacteristics(updateRequest.getCharacteristics());
                entity.setName(updateRequest.getName());
                session.merge(entity);
                session.lock(entity, LockModeType.NONE);
                transaction.commit();
                return rollbackUpdateEntity;
            }
            throw new NotFoundEntityException();
        } catch (ObjectOptimisticLockingFailureException | OptimisticLockException e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    @Transactional
    public DeleteGoodsFeedback deleteFromInventory(DeleteGoodsRequest dto, Acknowledgment ack) {
        GoodsEntity entity = goodsRepository.findById(dto.getItemId()).orElse(null);
        if (entity != null && entity.getSellerId() == dto.getSellerId()) {
            goodsRepository.delete(entity);
            ack.acknowledge();
            return new DeleteGoodsFeedback(dto.getId(), true, false);
        }
        ack.acknowledge();
        return new DeleteGoodsFeedback(dto.getId(), false, false);
    }

    @Override
    @Transactional
    public void rollbackUpdateInventory(String id, Acknowledgment ack) {
        //it persists 101%
        GoodsToRollbackUpdateEntity entity = rollbackUpdateRepository.findById(id).get();
        UpdateGoodsRequest request = updateRequestMapper.entityToDTO(entity);
        boolean updated = false;
        while (!updated) {
            try {
                updated = rollbackInventoryInner(request);
                ack.acknowledge();

            } catch (ObjectOptimisticLockingFailureException | OptimisticLockException e) {
                System.out.println(e.getMessage());
                //no need to do anything so that the algorithm works again
            } catch (NotFoundEntityException e) {
                return;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        }

    }

    private boolean rollbackInventoryInner(UpdateGoodsRequest updateRequest) throws NotFoundEntityException {
        EntityManager session = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = session.getTransaction();
        transaction.begin();
        try {
            GoodsEntity entity = session.find(GoodsEntity.class, updateRequest.getItemId());
            if (entity == null) {
                throw new NotFoundEntityException();
            }
            if (entity.getSellerId() == updateRequest.getSellerId()) {
                //сохраняем старую версию
                //amount кладется в toAddAmount
                GoodsToRollbackUpdateEntity rollbackUpdateEntity = goodsRollbackMapper.entityToRollbackEntity(entity);
                rollbackUpdateRepository.save(rollbackUpdateEntity);
                //добавляем количество к текущему
                session.lock(entity, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
                entity.setAmount(entity.getAmount() + updateRequest.getToAddAmount());
                entity.setCost(updateRequest.getCost());
                entity.setCategory(updateRequest.getCategory());
                entity.setCharacteristics(updateRequest.getCharacteristics());
                entity.setName(updateRequest.getName());
                session.merge(entity);
                session.lock(entity, LockModeType.NONE);
                transaction.commit();
            }
            return true;
        } catch (ObjectOptimisticLockingFailureException | OptimisticLockException e) {
            transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
