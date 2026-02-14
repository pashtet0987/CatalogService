package by.pashkavlushka.GoodsCatalogueService.repository;

import by.pashkavlushka.GoodsCatalogueService.entity.HandledAddEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HandledAddEventRepository extends JpaRepository<HandledAddEventEntity, String> {
    
}
