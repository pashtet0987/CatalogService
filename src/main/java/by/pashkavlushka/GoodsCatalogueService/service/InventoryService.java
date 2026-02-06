package by.pashkavlushka.GoodsCatalogueService.service;

import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import org.springframework.kafka.support.Acknowledgment;


public interface InventoryService {
    void addToInventory(GoodsDTO dto, Acknowledgment acknowledgment);
}
