package by.pashkavlushka.GoodsCatalogueService.repository;

import by.pashkavlushka.GoodsCatalogueService.entity.GoodsEntity;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface GoodsRepository extends JpaRepository<GoodsEntity, Long>, JpaSpecificationExecutor<GoodsEntity> {
    @EntityGraph(value = "goods_characteristics")
    Optional<GoodsEntity> findById(long id);
    
    @EntityGraph(value = "goods_characteristics")
    Page<GoodsEntity> findByCategory(String category, Pageable pageable);
    
    @EntityGraph(value = "goods_characteristics")
    Page<GoodsEntity> findBySellerId(long sellerId, Pageable pageable);
    
    @EntityGraph(value = "goods_characteristics")
    @Query("select g.amount from GoodsEntity g where id = :id")
    int findAmountById(@Param("id") long id);
    
    @EntityGraph(value = "goods_characteristics")
    @Query("select g from GoodsEntity g where category in :categories")
    Page<GoodsEntity> findByCategories(@Param("categories") List<String> categories, Pageable pageable);
    
    @Query("select distinct g.category from GoodsEntity g")
    List<String> findCategories();
    
    @EntityGraph(value = "goods_characteristics")
    @Query("select g from GoodsEntity g where category in :categories")
    List<GoodsEntity> findByCategories(@Param("categories") List<String> categories);
}
