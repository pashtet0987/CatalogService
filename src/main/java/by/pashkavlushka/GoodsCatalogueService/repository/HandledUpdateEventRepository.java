package by.pashkavlushka.GoodsCatalogueService.repository;

import by.pashkavlushka.GoodsCatalogueService.entity.HandledUpdateEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HandledUpdateEventRepository extends JpaRepository<HandledUpdateEventEntity, String> {
    
}
