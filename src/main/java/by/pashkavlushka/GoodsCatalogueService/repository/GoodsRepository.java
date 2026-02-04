package by.pashkavlushka.GoodsCatalogueService.repository;

import by.pashkavlushka.GoodsCatalogueService.entity.GoodsEntity;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface GoodsRepository extends JpaRepository<GoodsEntity, Long>{
    //@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<GoodsEntity> findById(long id);
    Page<GoodsEntity> findByCategory(String category, Pageable pageable);
    Page<GoodsEntity> findBySellerId(long sellerId, Pageable pageable);
    
    @Query("select g.amount from GoodsEntity g where id = :id")
    int findAmountById(@Param("id") long id);
    
    @Query("select g from GoodsEntity g where category in :categories")
    Page<GoodsEntity> findByCategories(@Param("categories") List<String> categories, Pageable pageable);
}
