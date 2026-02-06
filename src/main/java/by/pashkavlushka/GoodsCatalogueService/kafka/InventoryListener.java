package by.pashkavlushka.GoodsCatalogueService.kafka;

import by.pashkavlushka.GoodsCatalogueService.dto.AddGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.dto.GoodsDTO;
import by.pashkavlushka.GoodsCatalogueService.dto.UpdateGoodsRequest;
import by.pashkavlushka.GoodsCatalogueService.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class InventoryListener {

    private GoodsService goodsService;

    @Autowired
    public InventoryListener(GoodsService inventoryService) {
        this.goodsService = inventoryService;
    }

    @KafkaListener(topics = {"inventory-topic"}, groupId = "inventory", containerFactory = "kafkaListenerContainerFactory")
    public void addToInventory(AddGoodsRequest dto, Acknowledgment acknowledgment) {
        goodsService.addToInventory(dto, acknowledgment);
    }

    @KafkaListener(topics = {"update-inventory-topic"}, groupId = "update-inventory", containerFactory = "kafkaUpdateListenerContainerFactory")
    public void updateGoodsListener(UpdateGoodsRequest request, Acknowledgment ack) {
        goodsService.updateInventory(request, ack);
    }
}
