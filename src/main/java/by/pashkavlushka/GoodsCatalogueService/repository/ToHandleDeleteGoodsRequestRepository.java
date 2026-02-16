package by.pashkavlushka.GoodsCatalogueService.repository;

import by.pashkavlushka.GoodsCatalogueService.entity.DeleteGoodsRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;


public interface ToHandleDeleteGoodsRequestRepository extends JpaRepository<DeleteGoodsRequestEntity, String> {

}
