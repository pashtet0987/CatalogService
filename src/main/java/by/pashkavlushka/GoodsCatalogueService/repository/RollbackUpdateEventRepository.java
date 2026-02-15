package by.pashkavlushka.GoodsCatalogueService.repository;

import by.pashkavlushka.GoodsCatalogueService.entity.GoodsToRollbackUpdateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RollbackUpdateEventRepository extends JpaRepository<GoodsToRollbackUpdateEntity, String> {
    @Query("select e.sellerId from GoodsToRollbackUpdateEntity e where e.id = :id")
    Long findSellerIdById(@Param("id") String id);
    @Query("select count(e) from GoodsToRollbackUpdateEntity e where e.itemId = :itemId")
    int findInFlightUpdatesByItemId(@Param("itemId") long itemId);
}
