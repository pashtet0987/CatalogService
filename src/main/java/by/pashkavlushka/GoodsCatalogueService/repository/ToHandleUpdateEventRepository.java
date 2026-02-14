package by.pashkavlushka.GoodsCatalogueService.repository;

import by.pashkavlushka.GoodsCatalogueService.entity.ToHandleUpdateEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ToHandleUpdateEventRepository extends JpaRepository<ToHandleUpdateEventEntity, String> {
    @Query("select e.sellerId from ToHandleUpdateEventEntity e where e.id = :id")
    long findSellerIdById(@Param("id") String id);
}
