package by.pashkavlushka.GoodsCatalogueService.kafka;

import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.service.InventoryService;
import java.time.Instant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "inventory-topic")
public class InventoryListener {
    
    private InventoryService inventoryService;

    @Autowired
    public InventoryListener(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
    
    @KafkaHandler
    public void proceedInventoryChange(GoodsDTO dto, Acknowledgment acknowledgment) {
        inventoryService.addToInventory(dto, acknowledgment);
    }
}
